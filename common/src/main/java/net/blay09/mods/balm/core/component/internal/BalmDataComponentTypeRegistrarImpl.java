package net.blay09.mods.balm.core.component.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class BalmDataComponentTypeRegistrarImpl implements BalmDataComponentTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmDataComponentTypeRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <T> BalmDataComponentTypeRegistration<T> register(String name, BiFunction<ResourceLocation, DataComponentType.Builder<T>, DataComponentType.Builder<T>> constructor) {
        final var identifier = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.DATA_COMPONENT_TYPE, identifier);
        final var holder = registrar.register(resourceKey, (id) -> constructor.apply(id, createBuilder()).build());
        return new BalmDataComponentTypeRegistrationImpl<>(holder);
    }

    @Override
    public <T> DataComponentType.Builder<T> createBuilder() {
        return DataComponentType.builder();
    }

    private static final class BalmDataComponentTypeRegistrationImpl<T> implements BalmDataComponentTypeRegistration<T> {
        private final Holder<DataComponentType<T>> holder;

        @SuppressWarnings("unchecked")
        private BalmDataComponentTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<@NotNull DataComponentType<T>>) holder;
        }

        @Override
        public Holder<DataComponentType<T>> asHolder() {
            return holder;
        }
    }
}
