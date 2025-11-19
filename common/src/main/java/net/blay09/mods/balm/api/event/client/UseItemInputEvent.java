package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.InteractionHand;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ClientItemCallback.Use} instead.
 */
@Deprecated
public class UseItemInputEvent extends BalmEvent {

    private final InteractionHand hand;

    public UseItemInputEvent(InteractionHand hand) {
        this.hand = hand;
    }

    public InteractionHand getHand() {
        return hand;
    }
}
