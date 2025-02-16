package net.blay09.mods.balm.neoforge.compat.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.function.Predicate;

public class CuriosModCompat {
    public static boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player).map(trinkets -> trinkets.isEquipped(predicate)).orElse(false);
    }

    public static ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player).flatMap(trinkets -> trinkets.findFirstCurio(predicate)).map(SlotResult::stack).orElse(ItemStack.EMPTY);
    }

    public static List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player)
                .map(trinkets -> trinkets.findCurios(predicate).stream().map(SlotResult::stack).toList())
                .orElse(List.of());
    }
}
