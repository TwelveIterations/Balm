package net.blay09.mods.balm.api.event;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemCraftedEvent extends BalmEvent {
    private final Player player;
    private final ItemStack itemStack;
    private final Container craftMatrix;

    public ItemCraftedEvent(Player player, ItemStack itemStack, Container craftMatrix) {
        this.player = player;
        this.itemStack = itemStack;
        this.craftMatrix = craftMatrix;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Container getCraftMatrix() {
        return craftMatrix;
    }
}
