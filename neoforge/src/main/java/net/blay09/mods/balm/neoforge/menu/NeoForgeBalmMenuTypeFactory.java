package net.blay09.mods.balm.neoforge.menu;

import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.internal.AbstractBalmMenuTypeFactoryImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;

public class NeoForgeBalmMenuTypeFactory extends AbstractBalmMenuTypeFactoryImpl {
    public NeoForgeBalmMenuTypeFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> MenuType<TMenu> createMenuType(BalmMenuFactory<TMenu, TPayload> factory) {
        return new MenuType<>((IContainerFactory<TMenu>) (syncId, inventory, buf) -> factory.create(syncId,
                inventory,
                factory.getStreamCodec().decode(buf)),
                FeatureFlagSet.of(FeatureFlags.VANILLA));
    }
}
