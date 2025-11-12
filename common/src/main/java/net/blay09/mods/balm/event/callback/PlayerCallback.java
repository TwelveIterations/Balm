package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface PlayerCallback {
    @FunctionalInterface
    interface Attack {
        void handle(Player player, Entity target);

        EventMapper<Attack> EVENT = EventMapper.createUnbound();
    }
}
