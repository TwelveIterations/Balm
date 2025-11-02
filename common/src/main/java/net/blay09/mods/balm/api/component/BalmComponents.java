package net.blay09.mods.balm.api.component;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.Balm#registrar(net.minecraft.resources.ResourceKey, String)} instead.
 */
@Deprecated
public interface BalmComponents {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#registrar(net.minecraft.resources.ResourceKey, String)} instead.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    default <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, ResourceLocation identifier) {
        final var resourceKey = ResourceKey.create(Registries.DATA_COMPONENT_TYPE, identifier);
        final var holder = Balm.registrar().register(resourceKey, supplier::get);
        return new DeferredObject<>(identifier, () -> (DataComponentType<TComponent>) holder.value(), holder::isBound);
    }

    BalmComponents LEGACY = new BalmComponents() {
    };
}
