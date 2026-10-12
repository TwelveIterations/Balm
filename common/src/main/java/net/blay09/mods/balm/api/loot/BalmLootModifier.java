package net.blay09.mods.balm.api.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BalmLootModifier {
    /**
     * @deprecated Use {@link #apply(LootContext, List, ResourceKey)} which receives a loot table id, if available.
     */
    @Deprecated
    default void apply(LootContext context, List<ItemStack> loot) {
        apply(context, loot, null);
    }

    default void apply(LootContext context, List<ItemStack> loot, @Nullable ResourceKey<LootTable> lootTableId) {
        apply(context, loot);
    }
}
