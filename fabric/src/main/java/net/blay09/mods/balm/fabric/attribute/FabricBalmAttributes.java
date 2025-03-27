package net.blay09.mods.balm.fabric.attribute;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.attribute.BalmAttributes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class FabricBalmAttributes implements BalmAttributes {
    @Override
    public DeferredObject<Attribute> registerAttribute(Supplier<Attribute> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier,
                () -> Registry.register(BuiltInRegistries.ATTRIBUTE, identifier, supplier.get())).resolveImmediately();
    }
}
