package net.blay09.mods.balm.api.menu;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * @deprecated Use {@code Balm.menuTypes(namespace, initializer)} instead.
 */
@Deprecated
public interface BalmMenus {
    /**
     * @deprecated Use the scoped registrar via {@code Balm.menuTypes(namespace, initializer)} and {@link BalmMenuTypeRegistrar}
     */
    @Deprecated
    default <TMenu extends AbstractContainerMenu, TPayload> DeferredObject<MenuType<TMenu>> registerMenu(Identifier identifier, BalmMenuFactory<TMenu, TPayload> factory) {
        final var holder = Balm.getRuntime().menuTypes(identifier.getNamespace()).register(identifier.getPath(), factory).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmMenus LEGACY = new BalmMenus() {
    };
}
