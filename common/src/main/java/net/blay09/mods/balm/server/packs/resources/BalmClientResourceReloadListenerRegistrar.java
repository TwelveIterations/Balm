package net.blay09.mods.balm.server.packs.resources;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface BalmClientResourceReloadListenerRegistrar {
    void register(String name, PreparableReloadListener listener);

    void addDependency(Identifier first, Identifier second);
}
