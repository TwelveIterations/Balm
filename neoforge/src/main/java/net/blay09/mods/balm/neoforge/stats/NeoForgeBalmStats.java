package net.blay09.mods.balm.neoforge.stats;

import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NeoForgeBalmStats implements BalmStats {

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public void registerCustomStat(ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.CUSTOM_STAT, identifier.getNamespace());
        register.register(identifier.getPath(), () -> identifier);
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

    private static class Registrations {
        public final List<ResourceLocation> customStats = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> customStats.forEach(it -> {
                Stats.CUSTOM.get(it, StatFormatter.DEFAULT);
            }));
        }
    }
}
