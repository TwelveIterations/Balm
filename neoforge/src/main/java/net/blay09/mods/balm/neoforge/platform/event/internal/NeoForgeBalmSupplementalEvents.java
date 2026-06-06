package net.blay09.mods.balm.neoforge.platform.event.internal;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.callback.LivingEntityCallback;

public class NeoForgeBalmSupplementalEvents {
    public static final Event<LivingEntityCallback.Death.Before> BEFORE_DEATH = EventFactory.createArrayBacked(LivingEntityCallback.Death.Before.class, (listeners) -> (entity, damageSource, damage) -> {
        for (final var listener : listeners) {
            if (!listener.allowDeath(entity, damageSource, damage)) {
                return false;
            }
        }
        return true;
    });
}
