package net.blay09.mods.balm.api.loot;

import net.minecraft.resources.Identifier;

public interface BalmLootTables {
    void registerLootModifier(Identifier identifier, BalmLootModifier modifier);
}
