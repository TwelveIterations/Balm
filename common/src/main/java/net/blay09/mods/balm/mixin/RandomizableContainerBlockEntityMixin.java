package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.loot.UnpackedLootTableHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin implements UnpackedLootTableHolder {

    private ResourceKey<LootTable> unpackedLootTable;

    @Override
    public Optional<ResourceKey<LootTable>> balm$getUnpackedLootTable() {
        return Optional.ofNullable(unpackedLootTable);
    }

    @Override
    public void balm$setUnpackedLootTable(ResourceKey<LootTable> unpackedLootTable) {
        this.unpackedLootTable = unpackedLootTable;
    }
}
