package net.blay09.mods.balm.api.attribute;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public interface BalmAttributes {
    DeferredObject<Attribute> registerAttribute(Supplier<Attribute> supplier, ResourceLocation identifier);
}
