package net.blay09.mods.balm.forge.world.item;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.internal.AbstractBalmCreativeModeTabRegistrar;
import net.minecraft.world.item.CreativeModeTab;

public class ForgeBalmCreativeModeTabRegistrar extends AbstractBalmCreativeModeTabRegistrar {
    public ForgeBalmCreativeModeTabRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public CreativeModeTab.Builder createBuilder() {
        return CreativeModeTab.builder();
    }
}
