package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface PlayerCallback {
    interface Attack {
        @FunctionalInterface
        interface Before {
            boolean allowAttack(Player player, Entity target);

            EventMapper<Before> EVENT = EventMapper.createUnbound("PlayerCallback.Attack.Before");
        }
    }

    interface InteractWithEntity {
        @FunctionalInterface
        interface Before {
            InteractionEventResult handle(Player player, Level level, InteractionHand hand, Entity entity);

            EventMapper<Before> EVENT = EventMapper.createUnbound("PlayerCallback.Interact.Entity");
        }
    }
}
