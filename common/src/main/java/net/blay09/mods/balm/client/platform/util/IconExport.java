package net.blay09.mods.balm.client.platform.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class IconExport {
    private static final Logger logger = LoggerFactory.getLogger(IconExport.class);

    public static void export(String filter) {
        final var minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            RenderTarget renderTarget = null;
            try {
                renderTarget = new TextureTarget("balm_icon_export", 64, 64, true);

                CreativeModeTabs.tryRebuildTabContents(minecraft.player.connection.enabledFeatures(),
                        minecraft.options.operatorItemsTab().get(),
                        minecraft.level.registryAccess());
                final var colonIndex = filter.indexOf(':');
                final var filterModId = colonIndex != -1 ? filter.substring(0, colonIndex) : filter;
                final var filterItemId = colonIndex != -1 ? filter.substring(colonIndex + 1) : null;
                final var exportFolder = new File("exports/icons/" + filterModId);
                if (!exportFolder.exists() && !exportFolder.mkdirs()) {
                    throw new RuntimeException("Failed to create export folder: " + exportFolder);
                }

                for (final var creativeModeTab : CreativeModeTabs.allTabs()) {
                    for (final var itemStack : creativeModeTab.getDisplayItems()) {
                        final var itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                        if (!itemId.getNamespace().equals(filterModId) || (filterItemId != null && !itemId.getPath().equals(filterItemId))) {
                            continue;
                        }

                        // TODO 1.21.5: Render item to buffer
                        /*renderTarget.clear();
                        RenderSystem.enableDepthTest();
                        renderTarget.bindWrite(false);

                        final var matrix = new Matrix4f().setOrtho(0f, 16, 16, 0f, 1000f, 21000f);
                        RenderSystem.setProjectionMatrix(matrix, ProjectionType.ORTHOGRAPHIC);
                        final var modelViewStack = RenderSystem.getModelViewStack();
                        modelViewStack.pushMatrix();
                        modelViewStack.translation(0f, 0f, -11000f);
                        Lighting.setupForFlatItems();

                        guiGraphics.renderItem(itemStack, 0, 0);
                        guiGraphics.flush();

                        modelViewStack.popMatrix();
                        renderTarget.unbindWrite();
                        RenderSystem.disableDepthTest();*/

                        final var width = renderTarget.width;
                        final var height = renderTarget.height;
                        final var colorTexture = renderTarget.getColorTexture();
                        if (colorTexture == null) {
                            throw new IllegalStateException("Tried to capture screenshot of an incomplete framebuffer");
                        } else {
                            /* TODO 1.21.6: Copy buffer to image and save as file
                            final var buffer = RenderSystem.getDevice()
                                    .createBuffer(() -> "Screenshot buffer",
                                            BufferType.PIXEL_PACK,
                                            BufferUsage.STATIC_READ,
                                            width * height * colorTexture.getFormat().pixelSize());
                            final var commandEncoder = RenderSystem.getDevice().createCommandEncoder();
                            RenderSystem.getDevice().createCommandEncoder().copyTextureToBuffer(colorTexture, buffer, 0, () -> {
                                try (final var readview = commandEncoder.readBuffer(buffer)) {
                                    try (final var nativeImage = new NativeImage(width, height, false)) {
                                        for (int y = 0; y < height; y++) {
                                            for (int x = 0; x < width; x++) {
                                                final var color = readview.data().getInt((x + y * width) * colorTexture.getFormat().pixelSize());
                                                nativeImage.setPixelABGR(x, height - y - 1, color | -16777216);
                                            }
                                        }

                                        nativeImage.writeToFile(new File(exportFolder, itemId.getPath() + ".png"));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                buffer.close();
                            }, 0);*/
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to export icons", e);
            } finally {
                if (renderTarget != null) {
                    renderTarget.destroyBuffers();
                }
            }
        });
    }
}
