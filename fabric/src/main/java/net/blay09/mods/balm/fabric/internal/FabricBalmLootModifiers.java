package net.blay09.mods.balm.fabric.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.level.storage.loot.internal.CommonBalmLootTables;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public final class FabricBalmLootModifiers {

    private FabricBalmLootModifiers() {
    }

    public static void initialize() {
        // TODO LootTableEvents.MODIFY_DROPS.register((holder, lootContext, drops) -> applyModifiers(holder, drops, lootContext));
    }

    private static void applyModifiers(Holder<LootTable> holder, List<ItemStack> drops, LootContext lootContext) {
        final var lootModifiers = ((CommonBalmLootTables) Balm.lootModifiers()).lootModifiers;
        for (final var modifier : lootModifiers.values()) {
            modifier.apply(lootContext, drops, holder.unwrapKey().orElse(null));
        }
    }
}
