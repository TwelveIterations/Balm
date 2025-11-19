package net.blay09.mods.balm.world.ticks;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ticks.ContainerSingleItem;

public class DefaultContainerSingleItem implements ContainerSingleItem {
    private ItemStack itemStack = ItemStack.EMPTY;

    public DefaultContainerSingleItem() {
    }

    public DefaultContainerSingleItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getTheItem() {
        return itemStack;
    }

    @Override
    public void setTheItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot == 0) {
            return itemStack.split(amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        if (slot == 0) {
            this.itemStack = itemStack;
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
