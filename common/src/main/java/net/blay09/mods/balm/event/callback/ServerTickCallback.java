package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface ServerTickCallback {

    void handle(MinecraftServer server);

    EventMapper<ServerTickCallback> PRE = EventMapper.createUnbound("ServerTickCallback.PRE");
    EventMapper<ServerTickCallback> POST = EventMapper.createUnbound("ServerTickCallback.POST");

    @FunctionalInterface
    interface Entity {
        void handle(net.minecraft.world.entity.Entity entity);

        EventMapper<Entity> PRE = EventMapper.createUnbound("ServerTickCallback.Entity.PRE");
        EventMapper<Entity> POST = EventMapper.createUnbound("ServerTickCallback.Entity.POST");
    }

    @FunctionalInterface
    interface Level {
        void handle(ServerLevel level);

        EventMapper<Level> PRE = EventMapper.createUnbound("ServerTickCallback.Level.PRE");
        EventMapper<Level> POST = EventMapper.createUnbound("ServerTickCallback.Level.POST");
    }

    @FunctionalInterface
    interface Player {
        void handle(ServerPlayer player);

        EventMapper<Player> PRE = EventMapper.createUnbound("ServerTickCallback.Player.PRE");
        EventMapper<Player> POST = EventMapper.createUnbound("ServerTickCallback.Player.POST");
    }

}
