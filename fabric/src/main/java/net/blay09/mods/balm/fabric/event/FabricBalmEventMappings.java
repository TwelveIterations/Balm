package net.blay09.mods.balm.fabric.event;

import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.blay09.mods.balm.event.EventPhases;
import net.blay09.mods.balm.event.callback.ServerEntityTickCallback;
import net.blay09.mods.balm.event.callback.ServerLevelTickCallback;
import net.blay09.mods.balm.event.callback.ServerPlayerTickCallback;
import net.blay09.mods.balm.event.callback.ServerTickCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

public class FabricBalmEventMappings {
    private static final Map<ResourceLocation, ResourceLocation> PRIORITIES = Map.of(
            EventPhases.LOWEST, EventPhases.LOWEST,
            EventPhases.LOW, EventPhases.LOW,
            EventPhases.DEFAULT, net.fabricmc.fabric.api.event.Event.DEFAULT_PHASE,
            EventPhases.HIGH, EventPhases.HIGH,
            EventPhases.HIGHEST, EventPhases.HIGHEST
    );

    private static final Event<ServerPlayerTickCallback> SERVER_PLAYER_TICK_PRE = EventFactory.createArrayBacked(ServerPlayerTickCallback.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    private static final Event<ServerPlayerTickCallback> SERVER_PLAYER_TICK_POST = EventFactory.createArrayBacked(ServerPlayerTickCallback.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    private static final Event<ServerEntityTickCallback> SERVER_ENTITY_TICK_PRE = EventFactory.createArrayBacked(ServerEntityTickCallback.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    private static final Event<ServerEntityTickCallback> SERVER_ENTITY_TICK_POST = EventFactory.createArrayBacked(ServerEntityTickCallback.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static void bind() {
        ServerTickCallback.PRE.setup((phase, it)
                -> ServerTickEvents.START_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.POST.setup((phase, it)
                -> ServerTickEvents.END_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerLevelTickCallback.PRE.setup((phase, it)
                -> ServerTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ServerLevelTickCallback.POST.setup((phase, it)
                -> ServerTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));

        ServerTickEvents.START_SERVER_TICK.register(mapPhase(EventPhases.DEFAULT), (MinecraftServer server) -> {
            if (SERVER_PLAYER_TICK_PRE.hasHandlers()) {
                for (final var player : server.getPlayerList().getPlayers()) {
                    SERVER_PLAYER_TICK_PRE.invoker().handle(player);
                }
            }
        });
        ServerPlayerTickCallback.PRE.setup((phase, it)
                -> SERVER_PLAYER_TICK_PRE.register(mapPhase(phase), it));
        ServerTickEvents.END_SERVER_TICK.register(mapPhase(EventPhases.DEFAULT), (MinecraftServer server) -> {
            if (SERVER_PLAYER_TICK_POST.hasHandlers()) {
                for (final var player : server.getPlayerList().getPlayers()) {
                    SERVER_PLAYER_TICK_POST.invoker().handle(player);
                }
            }
        });
        ServerPlayerTickCallback.POST.setup((phase, it)
                -> SERVER_PLAYER_TICK_POST.register(mapPhase(phase), it));

        ServerTickEvents.START_SERVER_TICK.register(mapPhase(EventPhases.DEFAULT), (MinecraftServer server) -> {
            if (SERVER_ENTITY_TICK_PRE.hasHandlers()) {
                for (final var level : server.getAllLevels()) {
                    for (final var entity : level.getAllEntities()) {
                        SERVER_ENTITY_TICK_PRE.invoker().handle(entity);
                    }
                }
            }
        });
        ServerEntityTickCallback.PRE.setup((phase, it) -> SERVER_ENTITY_TICK_PRE.register(mapPhase(phase), it));
        ServerTickEvents.END_SERVER_TICK.register(mapPhase(EventPhases.DEFAULT), (MinecraftServer server) -> {
            if (SERVER_ENTITY_TICK_POST.hasHandlers()) {
                for (final var level : server.getAllLevels()) {
                    for (final var entity : level.getAllEntities()) {
                        SERVER_ENTITY_TICK_POST.invoker().handle(entity);
                    }
                }
            }
        });
        ServerEntityTickCallback.POST.setup((phase, it) -> SERVER_ENTITY_TICK_POST.register(mapPhase(phase), it));
    }

    public static ResourceLocation mapPhase(ResourceLocation phase) {
        return PRIORITIES.getOrDefault(phase, Event.DEFAULT_PHASE);
    }
}
