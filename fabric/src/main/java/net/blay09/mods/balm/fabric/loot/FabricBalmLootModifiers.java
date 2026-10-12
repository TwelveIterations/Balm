package net.blay09.mods.balm.fabric.loot;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.fabric.mixin.FabricLootTableAccess;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collection;
import java.util.List;

public final class FabricBalmLootModifiers {
    private static volatile Collection<BalmLootModifier> modifiers = List.of();

    private FabricBalmLootModifiers() {
    }

    public static void initialize() {
        final var lootTables = (CommonBalmLootTables) Balm.lootModifiers();
        modifiers = lootTables.lootModifiers.values();
        LootTableEvents.ALL_LOADED.register((resourceManager, lootRegistry) -> {
            for (final var entry : lootRegistry.entrySet()) {
                ((FabricLootTableAccess) entry.getValue()).balm$setLootTableId(entry.getKey());
            }
        });
    }

    public static Collection<BalmLootModifier> getModifiers() {
        return modifiers;
    }

    public static void applyModifiers(Collection<BalmLootModifier> modifiers, ResourceKey<LootTable> lootTableId, LootContext lootContext, List<ItemStack> drops) {
        for (final BalmLootModifier modifier : modifiers) {
            modifier.apply(lootContext, drops, lootTableId);
        }
    }
}