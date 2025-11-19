package net.blay09.mods.balm.world.level.storage.loot;

import net.minecraft.resources.Identifier;

public interface BalmLootTables {
    void registerLootModifier(Identifier identifier, BalmLootModifier modifier);
}
