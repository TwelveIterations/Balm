package net.blay09.mods.balm.server.packs.resources;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface BalmClientResourceReloadListenerRegistrar {
    void register(String name, PreparableReloadListener listener);

    void addDependency(Identifier first, Identifier second);

    VanillaKeys vanillaKeys();

    interface VanillaKeys {
        Identifier blockEntityRenderDispatcher();
        Identifier cloudRenderer();
        Identifier equipmentAssets();
        Identifier entityRenderDispatcher();
        Identifier dryFoliageColor();
        Identifier foliageColor();
        Identifier fonts();
        Identifier grassColor();
        Identifier atlas();
        Identifier languages();
        Identifier models();
        Identifier particles();
        Identifier shaders();
        Identifier sounds();
        Identifier splashTexts();
        Identifier textures();
        Identifier waypointStyle();
    }
}
