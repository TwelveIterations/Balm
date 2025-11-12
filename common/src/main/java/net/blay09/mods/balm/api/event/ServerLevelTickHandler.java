package net.blay09.mods.balm.api.event;

import net.minecraft.world.level.Level;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerTickCallback.Level} instead.
 */
@Deprecated
public interface ServerLevelTickHandler {
    void handle(Level level);
}
