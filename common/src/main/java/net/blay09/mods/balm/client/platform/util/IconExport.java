package net.blay09.mods.balm.client.platform.util;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemDisplayContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

public class IconExport {
    private static final Logger logger = LoggerFactory.getLogger(IconExport.class);
    private static final int EXPORT_SIZE = 64;
    private static final int ITEM_RENDER_SCALE = EXPORT_SIZE;

    public static void export(String filter) {
        final var minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            final var player = minecraft.player;
            final var level = minecraft.level;
            if (player != null && level != null) {
                CreativeModeTabs.tryRebuildTabContents(player.connection.enabledFeatures(),
                        minecraft.options.operatorItemsTab().get(),
                        level.registryAccess());
            }

            final var colonIndex = filter.indexOf(':');
            final var filterModId = colonIndex != -1 ? filter.substring(0, colonIndex) : filter;
            final var filterItemId = colonIndex != -1 ? filter.substring(colonIndex + 1) : null;
            final var exportFolder = new File("exports/icons/" + filterModId);
            if (!exportFolder.exists() && !exportFolder.mkdirs()) {
                throw new RuntimeException("Failed to create export folder: " + exportFolder);
            }

            final var exportedItems = new HashSet<Identifier>();
            try (final var projectionMatrixBuffer = new ProjectionMatrixBuffer("balm_icon_export")) {
                for (final var creativeModeTab : CreativeModeTabs.allTabs()) {
                    for (final var itemStack : creativeModeTab.getDisplayItems()) {
                        final var itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                        if (!itemId.getNamespace().equals(filterModId)
                                || (filterItemId != null && !itemId.getPath().equals(filterItemId))
                                || !exportedItems.add(itemId)) {
                            continue;
                        }

                        exportItem(minecraft, projectionMatrixBuffer, exportFolder, itemId, itemStack);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to export icons", e);
            }
        });
    }

    private static void exportItem(Minecraft minecraft, ProjectionMatrixBuffer projectionMatrixBuffer, File exportFolder, Identifier itemId, net.minecraft.world.item.ItemStack itemStack) {
        RenderTarget renderTarget = null;
        GpuBuffer screenshotBuffer = null;
        try {
            renderTarget = new TextureTarget("balm_icon_export", EXPORT_SIZE, EXPORT_SIZE, true);
            final var colorTexture = Objects.requireNonNull(renderTarget.getColorTexture(), "color texture missing");
            final var depthTexture = Objects.requireNonNull(renderTarget.getDepthTexture(), "depth texture missing");
            final var offscreenCommandEncoder = RenderSystem.getDevice().createCommandEncoder();

            final var projection = new Projection();
            projection.setupOrtho(-1000f, 1000f, EXPORT_SIZE, EXPORT_SIZE, true);

            offscreenCommandEncoder.clearColorAndDepthTextures(colorTexture, 0, depthTexture, 0.0);

            final var gameRenderer = minecraft.gameRenderer;
            final var trackingState = new TrackingItemStackRenderState();
            final var submitNodeStorage = new SubmitNodeStorage();
            minecraft.getItemModelResolver().updateForTopItem(trackingState, itemStack, ItemDisplayContext.GUI, minecraft.level, minecraft.player, 0);

            final var poseStack = new PoseStack();
            poseStack.translate(EXPORT_SIZE / 2f, EXPORT_SIZE / 2f, 0f);
            poseStack.scale(ITEM_RENDER_SCALE, -ITEM_RENDER_SCALE, ITEM_RENDER_SCALE);

            final var previousColorOverride = RenderSystem.outputColorTextureOverride;
            final var previousDepthOverride = RenderSystem.outputDepthTextureOverride;
            try {
                RenderSystem.backupProjectionMatrix();
                RenderSystem.setProjectionMatrix(projectionMatrixBuffer.getBuffer(projection), ProjectionType.ORTHOGRAPHIC);
                RenderSystem.outputColorTextureOverride = renderTarget.getColorTextureView();
                RenderSystem.outputDepthTextureOverride = renderTarget.getDepthTextureView();

                if (trackingState.usesBlockLight()) {
                    gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_3D);
                } else {
                    gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_FLAT);
                }

                trackingState.submit(poseStack, submitNodeStorage, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
                gameRenderer.featureRenderDispatcher().renderAllFeatures(submitNodeStorage);
                gameRenderer.renderBuffers().endFrame();
            } finally {
                RenderSystem.outputColorTextureOverride = previousColorOverride;
                RenderSystem.outputDepthTextureOverride = previousDepthOverride;
                RenderSystem.restoreProjectionMatrix();
            }

            final var pixelSize = colorTexture.getFormat().pixelSize();
            screenshotBuffer = RenderSystem.getDevice().createBuffer(
                    () -> "balm_icon_export_buffer",
                    GpuBuffer.USAGE_MAP_READ | GpuBuffer.USAGE_COPY_DST,
                    (long) EXPORT_SIZE * EXPORT_SIZE * pixelSize);

            final var targetFile = new File(exportFolder, itemId.getPath() + ".png");
            final var readCommandEncoder = RenderSystem.getDevice().createCommandEncoder();
            final var bufferToRead = screenshotBuffer;
            final var renderTargetToDestroy = renderTarget;
            renderTarget = null;
            screenshotBuffer = null;
            offscreenCommandEncoder.copyTextureToBuffer(
                    colorTexture,
                    bufferToRead,
                    0,
                    () -> writeExportedImage(readCommandEncoder, bufferToRead, renderTargetToDestroy, targetFile),
                    0);
            offscreenCommandEncoder.submit();
        } catch (Exception e) {
            if (screenshotBuffer != null) {
                screenshotBuffer.close();
            }
            if (renderTarget != null) {
                renderTarget.destroyBuffers();
            }
            throw new RuntimeException("Failed to export icon for " + itemId, e);
        }
    }

    private static void writeExportedImage(CommandEncoder readCommandEncoder, GpuBuffer screenshotBuffer, RenderTarget renderTarget, File targetFile) {
        try (screenshotBuffer;
             final var readView = readCommandEncoder.mapBuffer(screenshotBuffer, true, false);
             final var nativeImage = new NativeImage(EXPORT_SIZE, EXPORT_SIZE, false)) {
            final var byteBuffer = readView.data();
            for (int y = 0; y < EXPORT_SIZE; y++) {
                final var targetY = EXPORT_SIZE - y - 1;
                for (int x = 0; x < EXPORT_SIZE; x++) {
                    final var color = byteBuffer.getInt((x + y * EXPORT_SIZE) * Integer.BYTES);
                    nativeImage.setPixelABGR(x, targetY, color);
                }
            }

            nativeImage.writeToFile(targetFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write exported icon: " + targetFile, e);
        } finally {
            renderTarget.destroyBuffers();
        }
    }

}
