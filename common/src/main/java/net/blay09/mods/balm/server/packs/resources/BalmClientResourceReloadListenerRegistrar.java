package net.blay09.mods.balm.server.packs.resources;

import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface BalmClientResourceReloadListenerRegistrar {
    void register(String name, PreparableReloadListener listener);
}
