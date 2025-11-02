package net.blay09.mods.balm.api.resources;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public interface BalmResources {
    <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec);

    void visitModResources(String modId, String path, ModResourceVisitor visitor);

    Optional<ModResource> lookupModResource(String modId, String path);
}
