package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface ServerTickCallback {

    void handle(MinecraftServer server);

    EventMapper<ServerTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ServerTickCallback> POST = EventMapper.createUnbound();

    @FunctionalInterface
    interface Entity {
        void handle(net.minecraft.world.entity.Entity entity);

        EventMapper<Entity> PRE = EventMapper.createUnbound();
        EventMapper<Entity> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Level {
        void handle(ServerLevel level);

        EventMapper<Level> PRE = EventMapper.createUnbound();
        EventMapper<Level> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Player {
        void handle(ServerPlayer player);

        EventMapper<Player> PRE = EventMapper.createUnbound();
        EventMapper<Player> POST = EventMapper.createUnbound();
    }

}
