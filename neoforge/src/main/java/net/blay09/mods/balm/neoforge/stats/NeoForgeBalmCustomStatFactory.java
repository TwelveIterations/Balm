package net.blay09.mods.balm.neoforge.stats;

import net.blay09.mods.balm.api.stats.AbstractBalmCustomStatFactoryImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeBalmCustomStatFactory extends AbstractBalmCustomStatFactoryImpl {

    public NeoForgeBalmCustomStatFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public ResourceLocation register(String name, StatFormatter formatter) {
        final var stat = super.register(name, formatter);
        ModBusEventRegisters.getRegistrations(namespace, Registrations.class).customStats.add(stat);
        return stat;
    }

    public static class Registrations {
        public final List<ResourceLocation> customStats = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> customStats.forEach(it -> Stats.CUSTOM.get(it, StatFormatter.DEFAULT)));
        }
    }
}
