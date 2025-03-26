package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TossItemEvent extends BalmEvent {
    private final Player player;
    private final ItemStack itemStack;

    public TossItemEvent(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
