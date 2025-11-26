package net.blay09.mods.balm.api.component;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#dataComponentTypes(Consumer)} instead.
 */
@Deprecated
public interface BalmComponents {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#dataComponentTypes(Consumer)} instead.
     */
    @Deprecated
    <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, ResourceLocation identifier);
}
