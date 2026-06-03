package com.example.balm;

import net.blay09.mods.balm.platform.config.BalmConfig;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.blay09.mods.balm.world.item.BalmCompostableRegistrar;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootTables;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class MiscTestModule implements BalmModule {
    @Override
    public Identifier getId() {
        return BalmExample.id("misc_test");
    }

    @Override
    public void registerConfig(BalmConfig config) {
        config.registerConfig(ExampleConfig.class);
    }

    @Override
    public void registerCompostables(BalmCompostableRegistrar compostables) {
        compostables.register(Items.DIAMOND, 1f);
    }

    @Override
    public void registerLootTables(BalmLootTables lootTables) {
        lootTables.registerLootModifier(BalmExample.id("loot_modifier"), new BalmLootModifier() {
            @Override
            public void apply(LootContext context, List<ItemStack> loot, @Nullable ResourceKey<LootTable> lootTableId) {
                if (lootTableId != null && lootTableId.identifier().equals(Identifier.withDefaultNamespace("blocks/grass_block"))) {
                    loot.add(new ItemStack(Items.DIAMOND));
                }
            }
        });
    }

    @Override
    public void initialize() {
        System.out.println("Hello common");
    }
}
