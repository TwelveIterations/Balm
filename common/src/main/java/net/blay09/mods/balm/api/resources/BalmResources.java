package net.blay09.mods.balm.api.resources;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public interface BalmResources {
    void registerResourceCondition(ResourceLocation identifier, MapCodec<BalmResourceCondition> codec);
}
