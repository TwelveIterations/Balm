package net.blay09.mods.balm.platform.compatibility.trinkets.internal;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class TrinketsUpdatedIntegration implements BalmModSupportTrinkets {
    @Override
    public boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getAttachment(player).isEquipped(predicate);
    }

    @Override
    public ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getAttachment(player).findFirst(predicate, true)
                .map(TrinketSlotAccess::get)
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getAttachment(player)
                .equipped(predicate, true)
                .stream().map(TrinketSlotAccess::get).toList();
    }
}
