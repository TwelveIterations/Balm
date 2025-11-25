package net.blay09.mods.balm.forge.container;

import net.blay09.mods.balm.api.container.ExtractionAwareContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BalmInvWrapper extends InvWrapper {
    public BalmInvWrapper(Container inv) {
        super(inv);
    }

    @SuppressWarnings("removal")
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (getInv() instanceof ExtractionAwareContainer extractionAwareContainer) {
            if (!extractionAwareContainer.canExtractItem(slot)) {
                return ItemStack.EMPTY;
            }
        }

        return super.extractItem(slot, amount, simulate);
    }
}