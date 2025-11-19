package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface ServerTickCallback {

    void handle(MinecraftServer server);

    EventMapper<ServerTickCallback> BEFORE = EventMapper.createUnbound("ServerTickCallback.Before");
    EventMapper<ServerTickCallback> AFTER = EventMapper.createUnbound("ServerTickCallback.After");

    @FunctionalInterface
    interface ServerEntityTick {
        void handle(Entity entity);

        EventMapper<ServerEntityTick> BEFORE = EventMapper.createUnbound("ServerEntityTick.Before");
        EventMapper<ServerEntityTick> AFTER = EventMapper.createUnbound("ServerEntityTick.After");
    }

    @FunctionalInterface
    interface ServerLevelTick {
        void handle(ServerLevel level);

        EventMapper<ServerLevelTick> BEFORE = EventMapper.createUnbound("ServerLevelTick.Before");
        EventMapper<ServerLevelTick> AFTER = EventMapper.createUnbound("ServerLevelTick.After");
    }

    @FunctionalInterface
    interface ServerPlayerTick {
        void handle(ServerPlayer player);

        EventMapper<ServerPlayerTick> BEFORE = EventMapper.createUnbound("ServerPlayerTick.Before");
        EventMapper<ServerPlayerTick> AFTER = EventMapper.createUnbound("ServerPlayerTick.After");
    }

}
