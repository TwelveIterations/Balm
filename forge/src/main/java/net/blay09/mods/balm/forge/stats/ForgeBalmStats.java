package net.blay09.mods.balm.forge.stats;

import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ForgeBalmStats implements BalmStats {

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public void registerCustomStat(ResourceLocation identifier) {
        getRegistrations(identifier.getNamespace()).customStats.add(identifier);

        final var registry = DeferredRegisters.get(Registries.CUSTOM_STAT, identifier.getNamespace());
        registry.register(identifier.getPath(), () -> identifier);
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
            event.enqueueWork(() -> customStats.forEach(it -> Stats.CUSTOM.get(it, StatFormatter.DEFAULT)));
        }
    }
}
