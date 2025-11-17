package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface ClientTickCallback {
    void handle(Minecraft client);

    EventMapper<ClientTickCallback> PRE = EventMapper.createUnbound("ClientTickCallback.PRE");
    EventMapper<ClientTickCallback> POST = EventMapper.createUnbound("ClientTickCallback.POST");

    @FunctionalInterface
    interface ClientPlayerTick {
        void handle(AbstractClientPlayer player);

        EventMapper<ClientPlayerTick> PRE = EventMapper.createUnbound("ClientTickCallback.Player.PRE");
        EventMapper<ClientPlayerTick> POST = EventMapper.createUnbound("ClientTickCallback.Player.POST");
    }

    @FunctionalInterface
    interface ClientLevelTick {
        void handle(ClientLevel level);

        EventMapper<ClientLevelTick> PRE = EventMapper.createUnbound("ClientTickCallback.Level.PRE");
        EventMapper<ClientLevelTick> POST = EventMapper.createUnbound("ClientTickCallback.Level.POST");
    }

    @FunctionalInterface
    interface ClientEntityTick {
        void handle(Entity entity);

        EventMapper<ClientEntityTick> PRE = EventMapper.createUnbound("ClientTickCallback.Entity.PRE");
        EventMapper<ClientEntityTick> POST = EventMapper.createUnbound("ClientTickCallback.Entity.POST");
    }

}
