package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CommonBalmLootTables implements BalmLootTables {
    public final Map<ResourceLocation, BalmLootModifier> lootModifiers = new HashMap<>();

    @Override
    public void registerLootModifier(ResourceLocation identifier, BalmLootModifier modifier) {
        lootModifiers.put(identifier, modifier);
    }

}
