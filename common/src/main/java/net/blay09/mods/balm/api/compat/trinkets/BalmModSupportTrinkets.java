package net.blay09.mods.balm.api.compat.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public interface BalmModSupportTrinkets {
    boolean isEquipped(Player player, Predicate<ItemStack> predicate);
    ItemStack findEquipped(Player player, Predicate<ItemStack> predicate);
    List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate);
}
