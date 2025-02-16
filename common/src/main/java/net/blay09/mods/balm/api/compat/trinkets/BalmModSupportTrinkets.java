package net.blay09.mods.balm.api.compat.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface BalmModSupportTrinkets {
    boolean isEquipped(Player player, Predicate<ItemStack> predicate);
}
