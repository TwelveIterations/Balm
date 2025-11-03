package net.blay09.mods.balm.forge.stats;

import net.blay09.mods.balm.api.stats.AbstractBalmCustomStatFactoryImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.forge.ModBusEventRegister;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeBalmCustomStatFactory extends AbstractBalmCustomStatFactoryImpl {

    public ForgeBalmCustomStatFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public ResourceLocation register(String name, StatFormatter formatter) {
        final var stat = super.register(name, formatter);
        ModBusEventRegisters.getRegistrations(namespace, Registrations.class).customStats.add(stat);
        return stat;
    }

    public static class Registrations implements ModBusEventRegister {
        public final List<ResourceLocation> customStats = new ArrayList<>();

        private void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> customStats.forEach(it -> Stats.CUSTOM.get(it, StatFormatter.DEFAULT)));
        }

        @Override
        public void register(BusGroup busGroup) {
            FMLCommonSetupEvent.getBus(busGroup).addListener(this::commonSetup);
        }
    }
}
