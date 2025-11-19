package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.event.EventHandling;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface PlayerCallback {
    @FunctionalInterface
    interface Attack {
        EventHandling handle(Player player, Entity target);

        EventMapper<Attack> EVENT = EventMapper.createUnbound("PlayerCallback.Attack");
    }
}
