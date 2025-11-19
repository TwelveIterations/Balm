package net.blay09.mods.balm.fabric.world.item.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.internal.AbstractBalmCreativeModeTabRegistrar;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;

public class FabricBalmCreativeModeTabRegistrar extends AbstractBalmCreativeModeTabRegistrar {
    public FabricBalmCreativeModeTabRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public CreativeModeTab.Builder createBuilder() {
        return FabricItemGroup.builder();
    }
}
