package net.blay09.mods.balm.world.entity.ai.village.poi;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.function.Supplier;

public interface BalmPoiTypeRegistrar {
    <T extends PoiType> Holder<T> register(String name, Supplier<T> supplier);
}
