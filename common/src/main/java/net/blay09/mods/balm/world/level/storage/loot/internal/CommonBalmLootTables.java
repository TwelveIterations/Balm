package net.blay09.mods.balm.world.level.storage.loot.internal;

import net.blay09.mods.balm.world.level.storage.loot.BalmLootModifier;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootTables;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommonBalmLootTables implements BalmLootTables {
    public final Map<Identifier, BalmLootModifier> lootModifiers = new ConcurrentHashMap<>();

    @Override
    public void registerLootModifier(Identifier identifier, BalmLootModifier modifier) {
        lootModifiers.put(identifier, modifier);
    }

}
