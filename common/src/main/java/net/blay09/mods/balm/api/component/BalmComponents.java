package net.blay09.mods.balm.api.component;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#registrar(net.minecraft.resources.ResourceKey, String)} instead.
 */
@Deprecated
public interface BalmComponents {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#registrar(net.minecraft.resources.ResourceKey, String)} instead.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    default <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, Identifier identifier) {
        final var resourceKey = ResourceKey.create(Registries.DATA_COMPONENT_TYPE, identifier);
        final var holder = Balm.getRuntime().registrar().register(resourceKey, (id) -> supplier.get());
        return new DeferredObject<>(identifier, () -> (DataComponentType<TComponent>) holder.value(), holder::isBound);
    }

    BalmComponents LEGACY = new BalmComponents() {
    };
}
