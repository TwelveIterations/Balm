package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class RenderHandEvent extends BalmEvent {

    private final InteractionHand hand;
    private final ItemStack itemStack;
    private final float swingProgress;

    public RenderHandEvent(InteractionHand hand, ItemStack itemStack, float swingProgress) {
        this.hand = hand;
        this.itemStack = itemStack;
        this.swingProgress = swingProgress;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public float getSwingProgress() {
        return swingProgress;
    }
}
