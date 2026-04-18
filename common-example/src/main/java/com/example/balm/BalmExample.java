package com.example.balm;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class BalmExample {
    public static void initialize(BalmRegistrars registrars) {
        Balm.config().registerConfig(ExampleConfig.class);

        registrars.compostables(registrar -> registrar.register(Items.DIAMOND, 1f));

        Balm.lootModifiers().registerLootModifier(Identifier.fromNamespaceAndPath("balm_example", "loot_modifier"), new BalmLootModifier() {
            @Override
            public void apply(LootContext context, List<ItemStack> loot, @Nullable ResourceKey<LootTable> lootTableId) {
                if (lootTableId != null && lootTableId.identifier().equals(Identifier.withDefaultNamespace("blocks/grass_block"))) {
                    loot.add(new ItemStack(Items.DIAMOND));
                }
            }
        });

        System.out.println("Hello common");
    }
}
