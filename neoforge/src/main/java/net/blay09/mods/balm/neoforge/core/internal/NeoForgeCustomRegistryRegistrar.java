package net.blay09.mods.balm.neoforge.core.internal;

import net.minecraft.core.Registry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeCustomRegistryRegistrar {
    private final List<Registry<?>> registries = new ArrayList<>();

    public <T> void add(Registry<T> registry) {
        registries.add(registry);
    }

    @SubscribeEvent
    public void registerRegistries(NewRegistryEvent event) {
        for (final var registry : registries) {
            event.register(registry);
        }
    }
}
