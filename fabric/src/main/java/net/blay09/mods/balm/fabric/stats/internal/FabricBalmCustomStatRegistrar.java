package net.blay09.mods.balm.fabric.stats.internal;

import net.blay09.mods.balm.stats.internal.AbstractBalmCustomStatRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class FabricBalmCustomStatRegistrar extends AbstractBalmCustomStatRegistrarImpl {

    public FabricBalmCustomStatRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public Identifier register(Identifier statIdentifier, StatFormatter formatter) {
        final var stat = super.register(statIdentifier, formatter);
        Stats.CUSTOM.get(stat, formatter);
        return stat;
    }
}
