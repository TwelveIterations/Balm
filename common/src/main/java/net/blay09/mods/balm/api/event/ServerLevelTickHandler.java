package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.event.callback.ServerTickCallback;
import net.minecraft.world.level.Level;

/**
 * @deprecated Use {@link ServerTickCallback.ServerLevelTick} instead.
 */
@Deprecated
public interface ServerLevelTickHandler {
    void handle(Level level);
}
