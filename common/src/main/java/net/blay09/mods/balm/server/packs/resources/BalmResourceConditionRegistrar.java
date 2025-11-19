package net.blay09.mods.balm.server.packs.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.resources.BalmResourceCondition;

public interface BalmResourceConditionRegistrar {
    <T extends BalmResourceCondition> void register(String name, MapCodec<T> codec);
}
