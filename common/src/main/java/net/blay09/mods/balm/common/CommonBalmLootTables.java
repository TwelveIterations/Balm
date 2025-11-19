package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.loot.BalmLootModifier;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class CommonBalmLootTables implements BalmLootTables {
    public final Map<Identifier, BalmLootModifier> lootModifiers = new HashMap<>();

    @Override
    public void registerLootModifier(Identifier identifier, BalmLootModifier modifier) {
        lootModifiers.put(identifier, modifier);
    }

}
