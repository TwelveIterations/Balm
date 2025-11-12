package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;

@FunctionalInterface
public interface ClientTickCallback {
    void handle(Minecraft client);

    EventMapper<ClientTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ClientTickCallback> POST = EventMapper.createUnbound();

    @FunctionalInterface
    interface Player {
        void handle(AbstractClientPlayer player);

        EventMapper<Player> PRE = EventMapper.createUnbound();
        EventMapper<Player> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Level {
        void handle(ClientLevel level);

        EventMapper<Level> PRE = EventMapper.createUnbound();
        EventMapper<Level> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Entity {
        void handle(Entity entity);

        EventMapper<Entity> PRE = EventMapper.createUnbound();
        EventMapper<Entity> POST = EventMapper.createUnbound();
    }

}
