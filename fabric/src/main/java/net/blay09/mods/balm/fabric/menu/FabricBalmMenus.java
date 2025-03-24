package net.blay09.mods.balm.fabric.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricBalmMenus implements BalmMenus {

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> DeferredObject<MenuType<TMenu>> registerMenu(ResourceLocation identifier, BalmMenuFactory<TMenu, TPayload> factory) {
        return new DeferredObject<>(identifier,
                () -> Registry.register(BuiltInRegistries.MENU,
                        identifier,
                        (MenuType<TMenu>) new ExtendedScreenHandlerType<>(factory::create, factory.getStreamCodec()))).resolveImmediately();
    }

}
