package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.event.EventPhases;
import net.blay09.mods.balm.event.callback.ServerEntityTickCallback;
import net.blay09.mods.balm.event.callback.ServerLevelTickCallback;
import net.blay09.mods.balm.event.callback.ServerPlayerTickCallback;
import net.blay09.mods.balm.event.callback.ServerTickCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.bus.EventBus;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.internal.Event;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class ForgeBalmEventMappings {
    private static final Map<ResourceLocation, Byte> PRIORITIES = Map.of(
            EventPhases.LOWEST, Priority.LOWEST,
            EventPhases.LOW, Priority.LOW,
            EventPhases.DEFAULT, Priority.NORMAL,
            EventPhases.HIGH, Priority.HIGH,
            EventPhases.HIGHEST, Priority.HIGHEST
    );

    public static void bind() {
        bindSimple(ServerTickCallback.PRE, TickEvent.ServerTickEvent.Pre.BUS, (event, it) -> it.handle(event.server()));
        bindSimple(ServerTickCallback.POST, TickEvent.ServerTickEvent.Post.BUS, (event, it) -> it.handle(event.server()));
        bindFiltered(ServerLevelTickCallback.PRE, TickEvent.LevelTickEvent.Pre.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerLevel) event.level()));
        bindFiltered(ServerLevelTickCallback.POST, TickEvent.LevelTickEvent.Post.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerLevel) event.level()));
        bindFiltered(ServerPlayerTickCallback.PRE, TickEvent.PlayerTickEvent.Pre.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerPlayer) event.player()));
        bindFiltered(ServerPlayerTickCallback.POST, TickEvent.PlayerTickEvent.Post.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerPlayer) event.player()));
        // TODO LivingEvent.LivingTickEvent only ticks for living entities and has no pre/post
        bindSimple(ServerEntityTickCallback.PRE, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));
        bindSimple(ServerEntityTickCallback.POST, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));
    }

    public static <TCallback, TEvent extends Event> void bindSimple(EventMapper<TCallback> mapper, EventBus<@NotNull TEvent> bus, BiConsumer<TEvent, TCallback> consumer) {
        mapper.setup((phase, listener) -> bus.addListener(mapPriority(phase), event -> consumer.accept(event, listener)));
    }

    public static <TCallback, TEvent extends Event> void bindFiltered(EventMapper<TCallback> mapper, EventBus<@NotNull TEvent> bus, Predicate<TEvent> filter, BiConsumer<TEvent, TCallback> consumer) {
        mapper.setup((phase, listener) -> bus.addListener(mapPriority(phase), event -> {
            if (filter.test(event)) {
                consumer.accept(event, listener);
            }
        }));
    }

    public static byte mapPriority(ResourceLocation phase) {
        return PRIORITIES.getOrDefault(phase, Priority.NORMAL);
    }
}
