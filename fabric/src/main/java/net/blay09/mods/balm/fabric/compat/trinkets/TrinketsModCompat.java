package net.blay09.mods.balm.fabric.compat.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class TrinketsModCompat {
    public static boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getTrinketComponent(player).map(trinkets -> trinkets.isEquipped(predicate)).orElse(false);
    }

    public static ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getTrinketComponent(player)
                .flatMap(trinkets -> trinkets.getEquipped(predicate).stream().findFirst())
                .map(Tuple::getB)
                .orElse(ItemStack.EMPTY);
    }

    public static List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getTrinketComponent(player)
                .map(trinkets -> trinkets.getEquipped(predicate).stream().map(Tuple::getB).toList())
                .orElse(List.of());
    }
}
