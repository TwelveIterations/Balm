package net.blay09.mods.balm.api.container;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DefaultContainer implements ImplementedContainer, WorldlyContainer {

    private NonNullList<ItemStack> items;

    public DefaultContainer(int size) {
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public DefaultContainer(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void deserialize(CompoundTag tag) {
        items = ImplementedContainer.deserializeInventory(tag, items.size());
    }

    public CompoundTag serialize() {
        return serializeInventory();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        final var slots = new int[items.size()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return true;
    }
}
