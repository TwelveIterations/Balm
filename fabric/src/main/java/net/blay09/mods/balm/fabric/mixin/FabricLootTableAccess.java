package net.blay09.mods.balm.fabric.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public interface FabricLootTableAccess {
    void balm$setLootTableId(ResourceKey<LootTable> lootTableId);
}