package net.blay09.mods.balm.api.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.Balm;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#resourceConditions(String, Consumer)} instead.
 */
@Deprecated
public interface BalmResources {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#resourceConditions(String, Consumer)} instead.
     */
    @Deprecated
    default <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        Balm.getRuntime().resourceConditions(identifier.getNamespace(), registrar -> registrar.register(identifier.getPath(), codec));
    }

    BalmResources LEGACY = new BalmResources() {
    };
}
