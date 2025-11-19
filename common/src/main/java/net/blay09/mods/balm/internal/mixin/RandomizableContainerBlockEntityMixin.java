package net.blay09.mods.balm.internal.mixin;

import net.blay09.mods.balm.world.level.storage.loot.UnpackedLootTableHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin implements UnpackedLootTableHolder {

    @Unique
    @Nullable
    private ResourceKey<LootTable> balm$unpackedLootTable;

    @Override
    public Optional<ResourceKey<LootTable>> balm$getUnpackedLootTable() {
        return Optional.ofNullable(balm$unpackedLootTable);
    }

    @Override
    public void balm$setUnpackedLootTable(ResourceKey<LootTable> unpackedLootTable) {
        this.balm$unpackedLootTable = unpackedLootTable;
    }
}
