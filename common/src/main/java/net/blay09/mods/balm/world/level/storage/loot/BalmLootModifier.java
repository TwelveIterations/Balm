package net.blay09.mods.balm.world.level.storage.loot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

@FunctionalInterface
public interface BalmLootModifier {
    void apply(LootContext context, List<ItemStack> loot);
}
