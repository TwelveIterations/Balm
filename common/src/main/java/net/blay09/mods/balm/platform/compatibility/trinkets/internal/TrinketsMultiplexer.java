package net.blay09.mods.balm.platform.compatibility.trinkets.internal;

import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class TrinketsMultiplexer implements BalmModSupportTrinkets {
    private final List<BalmModSupportTrinkets> providers;

    public TrinketsMultiplexer(List<BalmModSupportTrinkets> providers) {
        this.providers = providers;
    }

    @Override
    public boolean isEquipped(Player player, Predicate<ItemStack> predicate) {
        return providers.stream().anyMatch(provider -> provider.isEquipped(player, predicate));
    }

    @Override
    public ItemStack findEquipped(Player player, Predicate<ItemStack> predicate) {
        return providers.stream()
                .map(provider -> provider.findEquipped(player, predicate))
                .filter(stack -> !stack.isEmpty())
                .findFirst()
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public List<ItemStack> findAllEquipped(Player player, Predicate<ItemStack> predicate) {
        return providers.stream()
                .map(provider -> provider.findAllEquipped(player, predicate))
                .flatMap(List::stream)
                .toList();
    }
}
