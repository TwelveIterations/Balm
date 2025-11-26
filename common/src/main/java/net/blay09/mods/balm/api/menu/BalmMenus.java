package net.blay09.mods.balm.api.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#menuTypes(Consumer)} instead.
 */
@Deprecated
public interface BalmMenus {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#menuTypes(Consumer)} instead.
     */
    @Deprecated
    <TMenu extends AbstractContainerMenu, TPayload> DeferredObject<MenuType<TMenu>> registerMenu(ResourceLocation identifier, BalmMenuFactory<TMenu, TPayload> factory);
}
