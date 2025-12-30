package net.blay09.mods.balm.neoforge.world.entity.npc.villager.internal;

import net.blay09.mods.balm.world.entity.npc.villager.BalmVillagerTradeRegistrar;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeBalmVillagerTradeRegistrar implements BalmVillagerTradeRegistrar {

    private final VillagerTradesEvent event;

    public NeoForgeBalmVillagerTradeRegistrar(VillagerTradesEvent event) {
        this.event = event;
    }

    @Override
    public void registerTrade(ResourceKey<VillagerProfession> profession, int level, VillagerTrades.ItemListing... listings) {
        if (event.getType().equals(profession)) {
            final var trades = event.getTrades().computeIfAbsent(level, (key) -> new ArrayList<>());
            trades.addAll(List.of(listings));
        }
    }
}
