package net.blay09.mods.balm.fabric.world.inventory.internal;

import net.blay09.mods.balm.world.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.internal.AbstractBalmMenuTypeRegistrarImpl;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricBalmMenuTypeRegistrar extends AbstractBalmMenuTypeRegistrarImpl {
    public FabricBalmMenuTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> MenuType<TMenu> createMenuType(BalmMenuFactory<TMenu, TPayload> factory) {
        return new ExtendedMenuType<>(factory::create, factory.getStreamCodec());
    }
}
