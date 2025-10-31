package net.blay09.mods.balm.api.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;

public interface UnpackedLootTableHolder {
    Optional<ResourceKey<LootTable>> balm$getUnpackedLootTable();
    void balm$setUnpackedLootTable(ResourceKey<LootTable> unpackedLootTable);
}
