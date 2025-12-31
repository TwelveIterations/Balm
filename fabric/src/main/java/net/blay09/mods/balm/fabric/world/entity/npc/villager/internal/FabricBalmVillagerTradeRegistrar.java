package net.blay09.mods.balm.fabric.world.entity.npc.villager.internal;

import net.blay09.mods.balm.world.entity.npc.villager.BalmVillagerTradeRegistrar;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Arrays;

public class FabricBalmVillagerTradeRegistrar implements BalmVillagerTradeRegistrar {
    @Override
    public void registerTrade(ResourceKey<VillagerProfession> profession, int level, VillagerTrades.ItemListing... listings) {
        BuiltInRegistries.VILLAGER_PROFESSION.getOptional(profession).ifPresent(it
                -> TradeOfferHelper.registerVillagerOffers(it, level, output -> output.addAll(Arrays.asList(listings))));
    }
}
