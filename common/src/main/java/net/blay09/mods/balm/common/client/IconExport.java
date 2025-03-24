package net.blay09.mods.balm.common.client;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import org.joml.Matrix4f;
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
                renderTarget = new TextureTarget(64, 64, true);

                renderTarget.setClearColor(0f, 0f, 0f, 0f);
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

                final var guiGraphics = new GuiGraphics(minecraft, minecraft.renderBuffers().bufferSource());

                for (final var creativeModeTab : CreativeModeTabs.allTabs()) {
                    for (final var itemStack : creativeModeTab.getDisplayItems()) {
                        final var itemId = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                        if (!itemId.getNamespace().equals(filterModId) || (filterItemId != null && !itemId.getPath().equals(filterItemId))) {
                            continue;
                        }

                        renderTarget.clear();
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
                        RenderSystem.disableDepthTest();

                        try (final var nativeImage = new NativeImage(renderTarget.width, renderTarget.height, false)) {
                            RenderSystem.bindTexture(renderTarget.getColorTextureId());
                            nativeImage.downloadTexture(0, false);
                            nativeImage.flipY();
                            nativeImage.writeToFile(new File(exportFolder, itemId.getPath() + ".png"));
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
