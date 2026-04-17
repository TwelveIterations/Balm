package net.blay09.mods.balm.fabric.world.item.internal;

import net.blay09.mods.balm.world.item.BalmCompostableRegistrar;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.minecraft.world.level.ItemLike;

public class FabricBalmCompostableRegistrar implements BalmCompostableRegistrar {
    @Override
    public void register(ItemLike item, float value) {
        CompostableRegistry.INSTANCE.add(item, value);
    }
}
