package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface ServerTickCallback {

    void handle(MinecraftServer server);

    EventMapper<ServerTickCallback> PRE = EventMapper.createUnbound("ServerTickCallback.PRE");
    EventMapper<ServerTickCallback> POST = EventMapper.createUnbound("ServerTickCallback.POST");

    @FunctionalInterface
    interface ServerEntityTick {
        void handle(Entity entity);

        EventMapper<ServerEntityTick> PRE = EventMapper.createUnbound("ServerTickCallback.Entity.PRE");
        EventMapper<ServerEntityTick> POST = EventMapper.createUnbound("ServerTickCallback.Entity.POST");
    }

    @FunctionalInterface
    interface ServerLevelTick {
        void handle(ServerLevel level);

        EventMapper<ServerLevelTick> PRE = EventMapper.createUnbound("ServerTickCallback.Level.PRE");
        EventMapper<ServerLevelTick> POST = EventMapper.createUnbound("ServerTickCallback.Level.POST");
    }

    @FunctionalInterface
    interface ServerPlayerTick {
        void handle(ServerPlayer player);

        EventMapper<ServerPlayerTick> PRE = EventMapper.createUnbound("ServerTickCallback.Player.PRE");
        EventMapper<ServerPlayerTick> POST = EventMapper.createUnbound("ServerTickCallback.Player.POST");
    }

}
