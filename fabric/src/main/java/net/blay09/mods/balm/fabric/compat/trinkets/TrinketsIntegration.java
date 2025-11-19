package net.blay09.mods.balm.fabric.compat.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class TrinketsIntegration implements BalmModSupportTrinkets {
    @Override
    public boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getTrinketComponent(player).map(trinkets -> trinkets.isEquipped(predicate)).orElse(false);
    }

    @Override
    public ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getTrinketComponent(player)
                .flatMap(trinkets -> trinkets.getEquipped(predicate).stream().findFirst())
                .map(Tuple::getB)
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        return TrinketsApi.getTrinketComponent(player)
                .map(trinkets -> trinkets.getEquipped(predicate).stream().map(Tuple::getB).toList())
                .orElse(List.of());
    }
}
