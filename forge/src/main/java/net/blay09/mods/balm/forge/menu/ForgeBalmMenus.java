package net.blay09.mods.balm.forge.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmMenus implements BalmMenus {

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> DeferredObject<MenuType<TMenu>> registerMenu(ResourceLocation identifier, BalmMenuFactory<TMenu, TPayload> factory) {
        DeferredRegister<MenuType<?>> register = DeferredRegisters.get(ForgeRegistries.MENU_TYPES, identifier.getNamespace());
        // TODO we have to create a RegistryFriendlyByteBuf ourselves because Forge is out of date
        RegistryObject<MenuType<TMenu>> registryObject = register.register(identifier.getPath(),
                () -> new MenuType<>((IContainerFactory<TMenu>) (syncId, inventory, buf) -> factory.create(syncId,
                        inventory,
                        factory.getStreamCodec().decode(new RegistryFriendlyByteBuf(buf, inventory.player.registryAccess()))),
                        FeatureFlagSet.of(FeatureFlags.VANILLA)));
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

}
