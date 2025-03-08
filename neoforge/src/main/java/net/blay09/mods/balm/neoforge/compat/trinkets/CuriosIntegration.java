package net.blay09.mods.balm.neoforge.compat.trinkets;

import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class CuriosIntegration implements BalmModSupportTrinkets {
    @Override
    public boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player).map(trinkets -> trinkets.isEquipped(predicate)).orElse(false);
    }

    @Override
    public ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player).flatMap(trinkets -> trinkets.findFirstCurio(predicate)).map(SlotResult::stack).orElse(ItemStack.EMPTY);
    }

    @Override
    public List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(player)
                .map(trinkets -> trinkets.findCurios(predicate).stream().map(SlotResult::stack).toList())
                .orElse(List.of());
    }
}
