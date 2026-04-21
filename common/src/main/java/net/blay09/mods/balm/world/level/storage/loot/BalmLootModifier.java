package net.blay09.mods.balm.world.level.storage.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface BalmLootModifier {
    void apply(LootContext context, List<ItemStack> loot, @Nullable ResourceKey<LootTable> lootTableId);
}
