package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.blay09.mods.balm.event.callback.ServerTickCallback;
import net.minecraft.world.entity.Entity;

/**
 * @deprecated Use {@link ServerTickCallback.ServerEntityTick} or {@link ClientTickCallback.ClientEntityTick} instead.
 */
@Deprecated
public interface EntityTickHandler {
    void handle(Entity entity);
}
