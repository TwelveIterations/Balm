package net.blay09.mods.balm.fabric.menu;

import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.internal.AbstractBalmMenuTypeFactoryImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricBalmMenuTypeFactory extends AbstractBalmMenuTypeFactoryImpl {
    public FabricBalmMenuTypeFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> MenuType<TMenu> createMenuType(BalmMenuFactory<TMenu, TPayload> factory) {
        return new ExtendedScreenHandlerType<>(factory::create, factory.getStreamCodec());
    }
}
