package net.blay09.mods.balm.forge.compat.trinkets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;

public class CuriosModCompat {
    public static boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player).map(trinkets -> trinkets.isEquipped(predicate)).orElse(false);
    }
}
