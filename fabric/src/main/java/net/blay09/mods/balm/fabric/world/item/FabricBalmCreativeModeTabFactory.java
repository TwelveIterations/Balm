package net.blay09.mods.balm.fabric.world.item;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.AbstractBalmCreativeModeTabFactory;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;

public class FabricBalmCreativeModeTabFactory extends AbstractBalmCreativeModeTabFactory {
    public FabricBalmCreativeModeTabFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public CreativeModeTab.Builder createBuilder() {
        return FabricItemGroup.builder();
    }
}
