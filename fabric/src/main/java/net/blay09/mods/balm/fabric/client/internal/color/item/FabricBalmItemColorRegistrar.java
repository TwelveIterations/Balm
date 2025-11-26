package net.blay09.mods.balm.fabric.client.internal.color.item;

import net.blay09.mods.balm.client.color.item.BalmItemColorRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class FabricBalmItemColorRegistrar implements BalmItemColorRegistrar {
    public static final FabricBalmItemColorRegistrar INSTANCE = new FabricBalmItemColorRegistrar();

    @Override
    public void register(ItemColor color, Supplier<ItemLike[]> items) {
        ColorProviderRegistry.ITEM.register(color, items.get());
    }
}
