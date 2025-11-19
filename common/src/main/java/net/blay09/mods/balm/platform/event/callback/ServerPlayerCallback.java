package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;


public interface ServerPlayerCallback {

    @FunctionalInterface
    interface Connected {
        void handle(ServerPlayer player);

        EventMapper<Connected> EVENT = EventMapper.createUnbound("ServerPlayerCallback.Connected");
    }

    @FunctionalInterface
    interface Login {
        void handle(ServerPlayer player);

        EventMapper<Login> EVENT = EventMapper.createUnbound("ServerPlayerCallback.Login");
    }

    @FunctionalInterface
    interface Logout {
        void handle(ServerPlayer player);

        EventMapper<Logout> EVENT = EventMapper.createUnbound("ServerPlayerCallback.Logout");
    }

    @FunctionalInterface
    interface OpenMenu {
        void handle(ServerPlayer player, AbstractContainerMenu menu);

        EventMapper<OpenMenu> EVENT = EventMapper.createUnbound("ServerPlayerCallback.OpenMenu");
    }

    @FunctionalInterface
    interface DimensionChange {
        void handle(ServerPlayer player, ResourceKey<Level> from, ResourceKey<Level> to);

        EventMapper<DimensionChange> EVENT = EventMapper.createUnbound("ServerPlayerCallback.DimensionChange");
    }

    @FunctionalInterface
    interface Respawn {
        void handle(ServerPlayer oldPlayer, ServerPlayer newPlayer);

        EventMapper<Respawn> EVENT = EventMapper.createUnbound("ServerPlayerCallback.Respawn");
    }

    @FunctionalInterface
    interface ChunkTracking {
        void handle(ServerLevel level, ServerPlayer player, ChunkPos chunkPos);

        EventMapper<ChunkTracking> START = EventMapper.createUnbound("ServerPlayerCallback.ChunkTracking.START");
        EventMapper<ChunkTracking> STOP = EventMapper.createUnbound("ServerPlayerCallback.ChunkTracking.STOP");
    }

}
