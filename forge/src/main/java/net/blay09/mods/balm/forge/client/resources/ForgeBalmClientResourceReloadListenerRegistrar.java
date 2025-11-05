package net.blay09.mods.balm.forge.client.resources;

import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

public class ForgeBalmClientResourceReloadListenerRegistrar implements BalmClientResourceReloadListenerRegistrar {
    public static final ForgeBalmClientResourceReloadListenerRegistrar INSTANCE = new ForgeBalmClientResourceReloadListenerRegistrar();

    @Override
    public void register(String name, PreparableReloadListener listener) {
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(listener);
        }
    }
}
