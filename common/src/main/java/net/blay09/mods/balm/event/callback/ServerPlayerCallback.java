package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface ServerPlayerCallback {
    void handle(ServerPlayer player);

    EventMapper<ServerPlayerCallback> CONNECTED = EventMapper.createUnbound();
    EventMapper<ServerPlayerCallback> LOGIN = EventMapper.createUnbound();
    EventMapper<ServerPlayerCallback> LOGOUT = EventMapper.createUnbound();

    @FunctionalInterface
    interface OpenMenu {
        void handle(ServerPlayer player, AbstractContainerMenu menu);

        EventMapper<OpenMenu> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface DimensionChange {
        void handle(ServerPlayer player, ResourceKey<Level> from, ResourceKey<Level> to);

        EventMapper<DimensionChange> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Respawn {
        void handle(ServerPlayer oldPlayer, ServerPlayer newPlayer);

        EventMapper<Respawn> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface ChunkTracking {
        void handle(ServerLevel level, ServerPlayer player, ChunkPos chunkPos);

        EventMapper<ChunkTracking> START = EventMapper.createUnbound();
        EventMapper<ChunkTracking> STOP = EventMapper.createUnbound();
    }

}
