package net.blay09.mods.balm.fabric.compat.trinkets;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class FabricBalmModSupportTrinkets implements BalmModSupportTrinkets {
    @Override
    public boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        if (Balm.isModLoaded("trinkets")) {
            return TrinketsModCompat.isEquipped(player, predicate);
        }
        return false;
    }
}
