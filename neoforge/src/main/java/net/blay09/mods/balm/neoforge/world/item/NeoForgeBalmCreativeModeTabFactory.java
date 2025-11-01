package net.blay09.mods.balm.neoforge.world.item;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.AbstractBalmCreativeModeTabFactory;
import net.minecraft.world.item.CreativeModeTab;

public class NeoForgeBalmCreativeModeTabFactory extends AbstractBalmCreativeModeTabFactory {
    public NeoForgeBalmCreativeModeTabFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public CreativeModeTab.Builder createBuilder() {
        return CreativeModeTab.builder();
    }
}
