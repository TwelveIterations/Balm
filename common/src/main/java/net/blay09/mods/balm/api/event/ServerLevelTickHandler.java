package net.blay09.mods.balm.api.event;

import net.minecraft.world.level.Level;

public interface ServerLevelTickHandler {
    void handle(Level level);
}
