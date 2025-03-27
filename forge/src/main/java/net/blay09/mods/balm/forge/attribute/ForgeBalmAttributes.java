package net.blay09.mods.balm.forge.attribute;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.attribute.BalmAttributes;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class ForgeBalmAttributes implements BalmAttributes {
    @Override
    public DeferredObject<Attribute> registerAttribute(Supplier<Attribute> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.ATTRIBUTE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }
}
