package net.blay09.mods.balm.forge.compat.trinkets;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class ForgeBalmModSupportTrinkets implements BalmModSupportTrinkets {
    @Override
    public boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        if (Balm.isModLoaded("curios")) {
            return CuriosModCompat.isEquipped(player, predicate);
        }
        return false;
    }

    @Override
    public ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        if (Balm.isModLoaded("curios")) {
            return CuriosModCompat.findEquipped(player, predicate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        if (Balm.isModLoaded("curios")) {
            return CuriosModCompat.findAllEquipped(player, predicate);
        }
        return List.of();
    }
}
