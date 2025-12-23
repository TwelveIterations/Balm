package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonBalmLootTables implements BalmLootTables {
    public final Map<ResourceLocation, BalmLootModifier> lootModifiers = new ConcurrentHashMap<>();

    @Override
    public void registerLootModifier(ResourceLocation identifier, BalmLootModifier modifier) {
        lootModifiers.put(identifier, modifier);
    }

}
