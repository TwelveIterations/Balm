package net.blay09.mods.balm.fabric.compat.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class TrinketsModCompat {
    public static boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getTrinketComponent(player).map(trinkets -> trinkets.isEquipped(predicate)).orElse(false);
    }
}
