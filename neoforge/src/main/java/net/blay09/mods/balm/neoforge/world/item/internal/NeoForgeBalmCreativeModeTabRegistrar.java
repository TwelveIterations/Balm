package net.blay09.mods.balm.neoforge.world.item.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.internal.AbstractBalmCreativeModeTabRegistrar;
import net.minecraft.world.item.CreativeModeTab;

public class NeoForgeBalmCreativeModeTabRegistrar extends AbstractBalmCreativeModeTabRegistrar {
    public NeoForgeBalmCreativeModeTabRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public CreativeModeTab.Builder createBuilder() {
        return CreativeModeTab.builder();
    }
}
