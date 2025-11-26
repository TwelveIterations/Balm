package net.blay09.mods.balm.api.resources;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#resourceConditions(Consumer)} instead.
 */
@Deprecated
public interface BalmResources {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#resourceConditions(Consumer)} instead.
     */
    @Deprecated
    <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec);
}
