package net.blay09.mods.balm.api.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * @deprecated Use the scoped factory via {@code Balm.menuTypes(namespace, initializer)} and {@link BalmMenuTypeFactory}
 */
@Deprecated
public interface BalmMenus {
    /**
     * @deprecated Use the scoped factory via {@code Balm.menuTypes(namespace, initializer)} and {@link BalmMenuTypeFactory}
     */
    @Deprecated
    default <TMenu extends AbstractContainerMenu, TPayload> DeferredObject<MenuType<TMenu>> registerMenu(ResourceLocation identifier, BalmMenuFactory<TMenu, TPayload> factory) {
        final var holder = Balm.getRuntime().menuTypes(identifier.getNamespace()).register(identifier.getPath(), factory).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmMenus LEGACY = new BalmMenus() {
    };
}
