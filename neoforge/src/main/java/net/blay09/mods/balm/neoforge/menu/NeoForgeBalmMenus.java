package net.blay09.mods.balm.neoforge.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;

public class NeoForgeBalmMenus implements BalmMenus {

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> DeferredObject<MenuType<TMenu>> registerMenu(ResourceLocation identifier, BalmMenuFactory<TMenu, TPayload> factory) {
        final var register = DeferredRegisters.get(Registries.MENU, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(),
                () -> new MenuType<>((IContainerFactory<TMenu>) (syncId, inventory, buf) -> factory.create(syncId,
                        inventory,
                        factory.getStreamCodec().decode(buf)),
                        FeatureFlagSet.of(FeatureFlags.VANILLA)));
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

}
