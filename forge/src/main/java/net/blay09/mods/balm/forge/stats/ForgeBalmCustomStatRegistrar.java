package net.blay09.mods.balm.forge.stats;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.stats.internal.AbstractBalmCustomStatRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.forge.ModBusEventRegister;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeBalmCustomStatRegistrar extends AbstractBalmCustomStatRegistrarImpl {

    public ForgeBalmCustomStatRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public Identifier register(Identifier statIdentifier, StatFormatter formatter) {
        final var stat = super.register(statIdentifier, formatter);
        ModBusEventRegisters.getRegistrations(namespace, Registrations.class).customStats.add(Pair.of(stat, formatter));
        return stat;
    }

    public static class Registrations implements ModBusEventRegister {
        public final List<Pair<Identifier, StatFormatter>> customStats = new ArrayList<>();

        private void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> customStats.forEach(it -> Stats.CUSTOM.get(it.getFirst(), it.getSecond())));
        }

        @Override
        public void register(BusGroup busGroup) {
            FMLCommonSetupEvent.getBus(busGroup).addListener(this::commonSetup);
        }
    }
}
