package net.blay09.mods.balm.fabric.component;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricBalmComponents implements BalmComponents {
    @Override
    public <TComponent> DeferredObject<DataComponentType<TComponent>> registerComponent(Supplier<DataComponentType<TComponent>> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier,
                () -> Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, identifier, supplier.get())).resolveImmediately();
    }
}
