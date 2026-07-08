package net.blay09.mods.balm.client.platform.util;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalDouble;

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
        GpuTexture colorTexture = null;
        GpuTextureView colorTextureView = null;
        GpuTexture depthTexture = null;
        GpuTextureView depthTextureView = null;
        GpuBuffer screenshotBuffer = null;
        try {
            final var device = RenderSystem.getDevice();
            colorTexture = device.createTexture(() -> "balm_icon_export_color", 13, GpuFormat.RGBA8_UNORM, EXPORT_SIZE, EXPORT_SIZE, 1, 1);
            colorTextureView = device.createTextureView(colorTexture);
            depthTexture = device.createTexture(() -> "balm_icon_export_depth", 9, GpuFormat.D32_FLOAT, EXPORT_SIZE, EXPORT_SIZE, 1, 1);
            depthTextureView = device.createTextureView(depthTexture);

            final var offscreenCommandEncoder = RenderSystem.getDevice().createCommandEncoder();
            offscreenCommandEncoder.clearColorAndDepthTextures(colorTexture, GuiRenderer.CLEAR_COLOR, depthTexture, 0.0);

            final var gameRenderer = minecraft.gameRenderer;
            final var trackingState = new TrackingItemStackRenderState();
            final var submitNodeStorage = new SubmitNodeStorage();
            minecraft.getItemModelResolver().updateForTopItem(trackingState, itemStack, ItemDisplayContext.GUI, minecraft.level, minecraft.player, 0);

            final var poseStack = new PoseStack();
            poseStack.translate(EXPORT_SIZE / 2f, EXPORT_SIZE / 2f, 0f);
            poseStack.scale(ITEM_RENDER_SCALE, -ITEM_RENDER_SCALE, ITEM_RENDER_SCALE);

            try {
                RenderSystem.backupProjectionMatrix();
                final var projection = new Projection();
                projection.setupOrtho(-1000f, 1000f, EXPORT_SIZE, EXPORT_SIZE, true);
                RenderSystem.setProjectionMatrix(projectionMatrixBuffer.getBuffer(projection), ProjectionType.ORTHOGRAPHIC);

                if (trackingState.usesBlockLight()) {
                    gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_3D);
                } else {
                    gameRenderer.lighting().setupFor(Lighting.Entry.ITEMS_FLAT);
                }

                trackingState.submit(poseStack, submitNodeStorage, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
                try (final var frame = gameRenderer.featureRenderDispatcher().prepareFrame(submitNodeStorage);
                     final var renderPass = offscreenCommandEncoder.createRenderPass(
                             () -> "Balm icon export",
                             colorTextureView,
                             Optional.empty(),
                             depthTextureView,
                             OptionalDouble.empty())) {
                    RenderSystem.bindDefaultUniforms(renderPass);
                    FeatureRenderDispatcher.renderAllFeatures(renderPass, frame);
                }
            } finally {
                RenderSystem.restoreProjectionMatrix();
            }

            final var blockSize = colorTexture.getFormat().blockSize();
            screenshotBuffer = device.createBuffer(
                    () -> "balm_icon_export_buffer",
                    GpuBuffer.USAGE_MAP_READ | GpuBuffer.USAGE_COPY_DST,
                    (long) EXPORT_SIZE * EXPORT_SIZE * blockSize);

            final var targetFile = new File(exportFolder, itemId.getPath() + ".png");
            final var bufferToRead = screenshotBuffer;
            final var colorTextureToDestroy = colorTexture;
            final var colorTextureViewToDestroy = colorTextureView;
            final var depthTextureToDestroy = depthTexture;
            final var depthTextureViewToDestroy = depthTextureView;
            final var colorTextureToRead = colorTexture;
            colorTexture = null;
            colorTextureView = null;
            depthTexture = null;
            depthTextureView = null;
            screenshotBuffer = null;
            offscreenCommandEncoder.copyTextureToBuffer(
                    colorTextureToRead,
                    bufferToRead,
                    0,
                    () -> writeExportedImage(bufferToRead, colorTextureToDestroy, colorTextureViewToDestroy, depthTextureToDestroy, depthTextureViewToDestroy, targetFile),
                    0);
            offscreenCommandEncoder.submit();
        } catch (Exception e) {
            if (screenshotBuffer != null) {
                screenshotBuffer.close();
            }
            closeTextureResources(colorTexture, colorTextureView, depthTexture, depthTextureView);
            throw new RuntimeException("Failed to export icon for " + itemId, e);
        }
    }

    private static void writeExportedImage(
            GpuBuffer screenshotBuffer,
            GpuTexture colorTexture,
            GpuTextureView colorTextureView,
            GpuTexture depthTexture,
            GpuTextureView depthTextureView,
            File targetFile
    ) {
        try (screenshotBuffer;
             final var readView = screenshotBuffer.map(true, false);
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
            closeTextureResources(colorTexture, colorTextureView, depthTexture, depthTextureView);
        }
    }

    private static void closeTextureResources(
            @Nullable GpuTexture colorTexture,
            @Nullable GpuTextureView colorTextureView,
            @Nullable GpuTexture depthTexture,
            @Nullable GpuTextureView depthTextureView
    ) {
        if (colorTexture != null) {
            colorTexture.close();
        }
        if (colorTextureView != null) {
            colorTextureView.close();
        }
        if (depthTexture != null) {
            depthTexture.close();
        }
        if (depthTextureView != null) {
            depthTextureView.close();
        }
    }

}
