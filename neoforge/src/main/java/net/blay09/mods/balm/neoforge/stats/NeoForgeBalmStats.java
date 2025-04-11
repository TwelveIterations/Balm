package net.blay09.mods.balm.neoforge.stats;

import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public record NeoForgeBalmStats(NamespaceResolver namespaceResolver) implements BalmStats {

    @Override
    public void registerCustomStat(ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.CUSTOM_STAT, identifier.getNamespace());
        register.register(identifier.getPath(), () -> identifier);
        getActiveRegistrations().customStats.add(identifier);
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final List<ResourceLocation> customStats = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> customStats.forEach(it -> {
                Stats.CUSTOM.get(it, StatFormatter.DEFAULT);
            }));
        }
    }
}
