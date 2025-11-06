package net.blay09.mods.balm.neoforge.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class NeoForgeBalmResources implements BalmResources {
    @Override
    public <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        final var register = DeferredRegisters.get(NeoForgeRegistries.CONDITION_SERIALIZERS, identifier.getNamespace());
        register.register(identifier.getPath(),
                () -> codec.xmap(it -> new NeoForgeBalmResourceCondition<>(identifier, it, NeoForgeRegistries.CONDITION_SERIALIZERS::getValue),
                        NeoForgeBalmResourceCondition::delegate));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddServerReloadListenersEvent event) -> event.addListener(identifier, reloadListener.apply(event.getRegistryAccess())));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddServerReloadListenersEvent event) -> event.addListener(identifier,
                (ResourceManagerReloadListener) reloadListener::accept));
    }
}
