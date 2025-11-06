package net.blay09.mods.balm.api.resources;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public interface BalmResourceConditionRegistrar {
    <T extends BalmResourceCondition> void register(ResourceLocation identifier, MapCodec<T> codec);
}
