package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.Entity;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerTickCallback.Entity} or {@link net.blay09.mods.balm.client.event.callback.ClientTickCallback.Entity} instead.
 */
@Deprecated
public interface EntityTickHandler {
    void handle(Entity entity);
}
