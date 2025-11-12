package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;

@FunctionalInterface
public interface ClientTickCallback {
    void handle(Minecraft client);

    EventMapper<ClientTickCallback> PRE = EventMapper.createUnbound("ClientTickCallback.PRE");
    EventMapper<ClientTickCallback> POST = EventMapper.createUnbound("ClientTickCallback.POST");

    @FunctionalInterface
    interface Player {
        void handle(AbstractClientPlayer player);

        EventMapper<Player> PRE = EventMapper.createUnbound("ClientTickCallback.Player.PRE");
        EventMapper<Player> POST = EventMapper.createUnbound("ClientTickCallback.Player.POST");
    }

    @FunctionalInterface
    interface Level {
        void handle(ClientLevel level);

        EventMapper<Level> PRE = EventMapper.createUnbound("ClientTickCallback.Level.PRE");
        EventMapper<Level> POST = EventMapper.createUnbound("ClientTickCallback.Level.POST");
    }

    @FunctionalInterface
    interface Entity {
        void handle(net.minecraft.world.entity.Entity entity);

        EventMapper<Entity> PRE = EventMapper.createUnbound("ClientTickCallback.Entity.PRE");
        EventMapper<Entity> POST = EventMapper.createUnbound("ClientTickCallback.Entity.PRE");
    }

}
