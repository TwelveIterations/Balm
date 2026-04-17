package net.blay09.mods.balm.forge.world.item.internal;

import net.blay09.mods.balm.world.item.BalmCompostableRegistrar;
import net.minecraft.world.level.ItemLike;

public class ForgeBalmCompostableRegistrar implements BalmCompostableRegistrar {
    @Override
    public void register(ItemLike item, float value) {
        throw new UnsupportedOperationException("Compostables are not supported on Forge.");
    }
}
