package net.blay09.mods.balm.fabric.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.level.storage.loot.internal.CommonBalmLootTables;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public final class FabricBalmLootModifiers {

    private FabricBalmLootModifiers() {
    }

    public static void initialize() {
        LootTableEvents.MODIFY_DROPS.register((holder, lootContext, drops) -> applyModifiers(drops, lootContext));
    }

    private static void applyModifiers(List<ItemStack> drops, LootContext lootContext) {
        final var lootModifiers = ((CommonBalmLootTables) Balm.lootModifiers()).lootModifiers;
        for (final var modifier : lootModifiers.values()) {
            modifier.apply(lootContext, drops);
        }
    }
}
