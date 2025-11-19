package net.blay09.mods.balm.neoforge.stats;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.stats.internal.AbstractBalmCustomStatRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeBalmCustomStatRegistrar extends AbstractBalmCustomStatRegistrarImpl {

    public NeoForgeBalmCustomStatRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public Identifier register(Identifier statIdentifier, StatFormatter formatter) {
        final var stat = super.register(statIdentifier, formatter);
        ModBusEventRegisters.getRegistrations(namespace, Registrations.class).customStats.add(Pair.of(stat, formatter));
        return stat;
    }

    public static class Registrations {
        public final List<Pair<Identifier, StatFormatter>> customStats = new ArrayList<>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> customStats.forEach(it -> Stats.CUSTOM.get(it.getFirst(), it.getSecond())));
        }
    }
}
