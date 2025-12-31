package net.blay09.mods.balm.world.entity.npc.villager;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;

public interface BalmVillagerTradeRegistrar {
    void registerTrade(ResourceKey<VillagerProfession> profession, int level, VillagerTrades.ItemListing... listings);
}
