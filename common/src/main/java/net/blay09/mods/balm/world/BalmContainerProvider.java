package net.blay09.mods.balm.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public interface BalmContainerProvider {
    @Nullable
    Container getContainer();

    @Nullable
    default Container getContainer(Direction side) {
        return getContainer();
    }

    default void dropItems(Level level, BlockPos pos) {
        Container container = getContainer();
        if (container != null) {
            ContainerUtils.dropItems(container, level, pos);
        }
    }

    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        Container container = getContainer();
        if (container != null) {
            return ContainerUtils.extractItem(container, slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    default ItemStack insertItem(ItemStack itemStack, int slot, boolean simulate) {
        Container container = getContainer();
        if (container != null) {
            return ContainerUtils.insertItem(container, slot, itemStack, simulate);
        }
        return itemStack;
    }

    default ItemStack insertItemStacked(ItemStack itemStack, boolean simulate) {
        Container container = getContainer();
        if (container != null) {
            return ContainerUtils.insertItemStacked(container, itemStack, simulate);
        }
        return itemStack;
    }
}
