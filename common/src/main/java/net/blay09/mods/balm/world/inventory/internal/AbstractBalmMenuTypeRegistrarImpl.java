package net.blay09.mods.balm.world.inventory.internal;

import net.blay09.mods.balm.world.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistration;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBalmMenuTypeRegistrarImpl implements BalmMenuTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    protected AbstractBalmMenuTypeRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TPayload> BalmMenuTypeRegistration<TMenu> register(String name, BalmMenuFactory<TMenu, TPayload> factory) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.MENU, identifier);
        final var holder = registrar.register(resourceKey, id -> createMenuType(factory));
        return new BalmMenuTypeRegistrationImpl<>(holder);
    }

    private static class BalmMenuTypeRegistrationImpl<T extends AbstractContainerMenu> implements BalmMenuTypeRegistration<T> {
        private final Holder<MenuType<T>> holder;

        @SuppressWarnings("unchecked")
        private BalmMenuTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<@NotNull MenuType<T>>) holder;
        }

        @Override
        public Holder<MenuType<T>> asHolder() {
            return holder;
        }
    }
}
