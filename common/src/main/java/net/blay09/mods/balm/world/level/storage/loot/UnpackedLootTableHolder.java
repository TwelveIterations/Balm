package net.blay09.mods.balm.world.level.storage.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;

/**
 * @deprecated Use {@link BalmLootModifier#apply(LootContext, List, ResourceKey)} instead.
 */
@Deprecated
public interface UnpackedLootTableHolder {
    Optional<ResourceKey<LootTable>> balm$getUnpackedLootTable();
    void balm$setUnpackedLootTable(ResourceKey<LootTable> unpackedLootTable);
}
