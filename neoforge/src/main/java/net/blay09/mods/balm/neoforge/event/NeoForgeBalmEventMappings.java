package net.blay09.mods.balm.neoforge.event;

import net.blay09.mods.balm.api.event.server.ServerReloadFinishedEvent;
import net.blay09.mods.balm.event.CommonBalmSupplementalEvents;
import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.event.EventPhases;
import net.blay09.mods.balm.event.callback.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class NeoForgeBalmEventMappings {
    private static final Map<ResourceLocation, EventPriority> PRIORITIES = Map.of(
            EventPhases.LOWEST, EventPriority.LOWEST,
            EventPhases.LOW, EventPriority.LOW,
            EventPhases.DEFAULT, EventPriority.NORMAL,
            EventPhases.HIGH, EventPriority.HIGH,
            EventPhases.HIGHEST, EventPriority.HIGHEST
    );

    public static void bind() {
        bindSimple(ServerTickCallback.PRE, ServerTickEvent.Pre.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerTickCallback.POST, ServerTickEvent.Post.class, (event, it) -> it.handle(event.getServer()));
        bindFiltered(ServerTickCallback.Level.PRE, LevelTickEvent.Pre.class, event -> !event.getLevel().isClientSide(), (event, it) -> it.handle((ServerLevel) event.getLevel()));
        bindFiltered(ServerTickCallback.Level.POST, LevelTickEvent.Post.class, event -> !event.getLevel().isClientSide(), (event, it) -> it.handle((ServerLevel) event.getLevel()));
        bindFiltered(ServerTickCallback.Player.PRE, PlayerTickEvent.Pre.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindFiltered(ServerTickCallback.Player.POST, PlayerTickEvent.Post.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindFiltered(ServerTickCallback.Entity.PRE, EntityTickEvent.Pre.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));
        bindFiltered(ServerTickCallback.Entity.POST, EntityTickEvent.Post.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));

        bindSimple(ServerLifecycleCallback.STARTING, ServerAboutToStartEvent.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.STARTED, ServerStartedEvent.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.STOPPING, ServerStoppingEvent.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.STOPPED, ServerStoppedEvent.class, (event, it) -> it.handle(event.getServer()));
        ServerLifecycleCallback.RELOADING.setup(CommonBalmSupplementalEvents.SERVER_RELOADING::register);
        ServerLifecycleCallback.RELOADED.setup(CommonBalmSupplementalEvents.SERVER_RELOADED::register);
    }

    public static <TCallback, TEvent extends Event> void bindSimple(EventMapper<TCallback> mapper, Class<TEvent> eventClass, BiConsumer<TEvent, TCallback> consumer) {
        mapper.setup((phase, listener) -> NeoForge.EVENT_BUS.addListener(mapPriority(phase), eventClass, event -> consumer.accept(event, listener)));
    }

    public static <TCallback, TEvent extends Event> void bindFiltered(EventMapper<TCallback> mapper, Class<TEvent> eventClass, Predicate<TEvent> filter, BiConsumer<TEvent, TCallback> consumer) {
        mapper.setup((phase, listener) -> NeoForge.EVENT_BUS.addListener(mapPriority(phase), eventClass, event -> {
            if (filter.test(event)) {
                consumer.accept(event, listener);
            }
        }));
    }

    public static EventPriority mapPriority(ResourceLocation phase) {
        return PRIORITIES.getOrDefault(phase, EventPriority.NORMAL);
    }
}
