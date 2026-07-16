package net.blay09.mods.balm.server.packs.resources;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.function.Consumer;
import java.util.function.Function;

public interface BalmResourceReloadListenerRegistrar {
    default void register(String name, PreparableReloadListener listener) {
        register(name, (registries) -> listener);
    }

    void register(String name, Function<HolderLookup.Provider, PreparableReloadListener> listenerFactory);

    void register(String name, Consumer<ResourceManager> reloadListener);

    void addDependency(Identifier first, Identifier second);

    VanillaKeys vanillaKeys();

    interface VanillaKeys {
        Identifier functions();
    }
}
