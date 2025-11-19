package net.blay09.mods.balm.fabric.world.inventory;

import net.blay09.mods.balm.world.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.internal.AbstractBalmMenuTypeRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricBalmMenuTypeRegistrar extends AbstractBalmMenuTypeRegistrarImpl {
    public FabricBalmMenuTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> MenuType<TMenu> createMenuType(BalmMenuFactory<TMenu, TPayload> factory) {
        return new ExtendedScreenHandlerType<>(factory::create, factory.getStreamCodec());
    }
}
