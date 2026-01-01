package net.blay09.mods.balm.forge.platform.event.internal;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.blay09.mods.balm.platform.event.EventPhases;
import net.blay09.mods.balm.platform.event.callback.*;
import net.blay09.mods.balm.platform.event.internal.BalmSupplementalEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.bus.EventBus;
import net.minecraftforge.eventbus.api.event.characteristic.Cancellable;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.internal.Event;
import net.minecraftforge.fml.LogicalSide;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ForgeBalmEventMappings {
    private static final Map<Identifier, Byte> PRIORITIES = Map.of(
            EventPhases.LOWEST, Priority.LOWEST,
            EventPhases.LOW, Priority.LOW,
            EventPhases.DEFAULT, Priority.NORMAL,
            EventPhases.HIGH, Priority.HIGH,
            EventPhases.HIGHEST, Priority.HIGHEST
    );

    public static void bind() {
        bindSimple(ServerTickCallback.BEFORE, TickEvent.ServerTickEvent.Pre.BUS, (event, it) -> it.handle(event.server()));
        bindSimple(ServerTickCallback.AFTER, TickEvent.ServerTickEvent.Post.BUS, (event, it) -> it.handle(event.server()));
        bindFiltered(ServerTickCallback.ServerLevelTick.BEFORE, TickEvent.LevelTickEvent.Pre.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerLevel) event.level()));
        bindFiltered(ServerTickCallback.ServerLevelTick.AFTER, TickEvent.LevelTickEvent.Post.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerLevel) event.level()));
        bindFiltered(ServerTickCallback.ServerPlayerTick.BEFORE, TickEvent.PlayerTickEvent.Pre.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerPlayer) event.player()));
        bindFiltered(ServerTickCallback.ServerPlayerTick.AFTER, TickEvent.PlayerTickEvent.Post.BUS, event -> event.side() == LogicalSide.SERVER, (event, it) -> it.handle((ServerPlayer) event.player()));
        // TODO LivingEvent.LivingTickEvent only ticks for living entities and has no pre/post
        bindSimple(ServerTickCallback.ServerEntityTick.BEFORE, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));
        bindSimple(ServerTickCallback.ServerEntityTick.AFTER, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));

        bindSimple(ServerLifecycleCallback.Starting.EVENT, ServerAboutToStartEvent.BUS, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.Started.EVENT, ServerStartedEvent.BUS, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.Stopping.EVENT, ServerStoppingEvent.BUS, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.Stopped.EVENT, ServerStoppedEvent.BUS, (event, it) -> it.handle(event.getServer()));
        ServerLifecycleCallback.Reloading.EVENT.configureMapping(BalmSupplementalEvents.SERVER_RELOADING::register);
        ServerLifecycleCallback.Reloaded.EVENT.configureMapping(BalmSupplementalEvents.SERVER_RELOADED::register);

        bindCancelable(BlockCallback.DigSpeed.EVENT, PlayerEvent.BreakSpeed.BUS, (event, it) -> event.getPosition().map(pos -> {
            final var level = event.getEntity().level();
            final var speed = it.computeDigSpeed(level, pos, event.getState(), event.getEntity(), event.getNewSpeed());
            if (speed == -1f) {
                return true;
            }
            event.setNewSpeed(speed);
            return false;
        }).orElse(false));
        bindCancelable(BlockCallback.Break.Before.EVENT, BlockEvent.BreakEvent.BUS, (event, it) -> {
            final var level = event.getLevel();
            final var blockEntity = level.getBlockEntity(event.getPos());
            return !it.allowBreak(level, event.getPos(), event.getState(), blockEntity, event.getPlayer());
        });
        bindCancelable(BlockCallback.Use.EVENT, PlayerInteractEvent.RightClickBlock.BUS, (event, it) -> {
            final var result = it.handle(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            result.interactionResult().ifPresent(event::setCancellationResult);
            return result.interactionResult().isPresent();
        });

        bindCancelable(CommandCallback.Before.EVENT, CommandEvent.BUS, (event, it) -> !it.allowCommand(event.getParseResults()));

        ConfigCallback.Loaded.EVENT.configureMapping(BalmSupplementalEvents.CONFIG_LOADED::register);
        ConfigCallback.Reloaded.EVENT.configureMapping(BalmSupplementalEvents.CONFIG_RELOADED::register);

        bindSimple(CropCallback.Grow.Before.EVENT, BlockEvent.CropGrowEvent.Pre.BUS, (event, it) -> {
            final var result = it.beforeGrow(event.getLevel(), event.getPos(), event.getState());
            event.setResult(switch (result) {
                case DO_NOT_GROW -> Result.DENY;
                case GROW -> Result.ALLOW;
                default -> Result.DEFAULT;
            });
        });
        bindSimple(CropCallback.Grow.After.EVENT, BlockEvent.CropGrowEvent.Post.BUS, (event, it) -> it.afterGrow(event.getLevel(), event.getPos(), event.getState()));

        bindSimple(EntityCallback.AddedToLevel.EVENT, EntityJoinLevelEvent.BUS, (event, it) -> it.handle(event.getLevel(), event.getEntity()));
        bindCancelable(EntityCallback.DimensionChange.BEFORE, EntityTravelToDimensionEvent.BUS, (event, it) -> !it.allowDimensionChange(event.getEntity(), event.getEntity().level().dimension(), event.getDimension()));

        bindSimple(CreativeModeTabCallback.BuildContents.EVENT, BuildCreativeModeTabContentsEvent.BUS, (event, it) -> it.handle(event.getTab(), event));

        bindCancelable(ItemCallback.Use.EVENT, PlayerInteractEvent.RightClickItem.BUS, (event, it) -> {
            final var result = it.handle(event.getEntity(), event.getLevel(), event.getHand());
            result.interactionResult().ifPresent(event::setCancellationResult);
            return result.interactionResult().isPresent();
        });
        bindSimple(ItemCallback.Tooltip.EVENT, ItemTooltipEvent.BUS, (event, it) -> it.handle(event.getItemStack(), event.getToolTip(), event.getFlags()));
        bindSimple(ItemCallback.Craft.After.EVENT, PlayerEvent.ItemCraftedEvent.BUS, (event, it) -> it.afterCraft(event.getEntity(), event.getCrafting(), event.getContainer()));
        bindCancelable(ItemCallback.Toss.Before.EVENT, ItemTossEvent.BUS, (event, it) -> !it.allowToss(event.getPlayer(), event.getEntity().getItem()));

        bindSimple(LevelCallback.LOAD, LevelEvent.Load.BUS, (event, it) -> it.handle(event.getLevel()));
        bindSimple(LevelCallback.UNLOAD, LevelEvent.Unload.BUS, (event, it) -> it.handle(event.getLevel()));
        bindSimple(LevelCallback.Chunk.LOAD, ChunkEvent.Load.BUS, (event, it) -> it.handle(event.getLevel(), event.getChunk(), event.getChunk().getPos()));
        bindSimple(LevelCallback.Chunk.UNLOAD, ChunkEvent.Unload.BUS, (event, it) -> it.handle(event.getLevel(), event.getChunk(), event.getChunk().getPos()));

        bindSimple(LivingEntityCallback.Heal.Before.EVENT, LivingHealEvent.BUS, (event, it) -> event.setAmount(it.computeHeal(event.getEntity(), event.getAmount())));
        LivingEntityCallback.Fall.Before.EVENT.configureMapping(BalmSupplementalEvents.LIVING_FALL::register);
        bindSimple(LivingEntityCallback.Death.Before.EVENT, LivingDeathEvent.BUS, (event, it) -> it.allowDeath(event.getEntity(), event.getSource()));
        bindSimple(LivingEntityCallback.Damage.Before.EVENT, LivingDamageEvent.BUS, (event, it) -> it.computeDamage(event.getEntity(), event.getSource(), event.getAmount()));

        bindSimple(PlayerCallback.Attack.Before.EVENT, AttackEntityEvent.BUS, (event, it) -> it.allowAttack(event.getEntity(), event.getTarget()));

        bindSimple(ServerPlayerCallback.Join.EVENT, PlayerEvent.PlayerLoggedInEvent.BUS, (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.Leave.EVENT, PlayerEvent.PlayerLoggedOutEvent.BUS, (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.OpenMenu.EVENT, PlayerContainerEvent.Open.BUS, (event, it) -> it.handle((ServerPlayer) event.getEntity(), event.getContainer()));
        bindSimple(ServerPlayerCallback.DimensionChange.EVENT, PlayerEvent.PlayerChangedDimensionEvent.BUS, (event, it) -> it.handle((ServerPlayer) event.getEntity(), event.getFrom(), event.getTo()));
        // TODO passing same entity twice currently
        bindSimple(ServerPlayerCallback.Respawn.EVENT, PlayerEvent.PlayerRespawnEvent.BUS, (event, it) -> it.handle((ServerPlayer) event.getEntity(), (ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.ChunkTracking.START, ChunkWatchEvent.Watch.BUS, (event, it) -> it.handle(event.getLevel(), event.getPlayer(), event.getPos()));
        bindSimple(ServerPlayerCallback.ChunkTracking.STOP, ChunkWatchEvent.UnWatch.BUS, (event, it) -> it.handle(event.getLevel(), event.getPlayer(), event.getPos()));
    }

    public static <TCallback, TEvent extends Event> void bindSimple(EventMapper<TCallback> mapper, EventBus<TEvent> bus, BiConsumer<TEvent, TCallback> consumer) {
        mapper.configureMapping((phase, listener) -> bus.addListener(mapPriority(phase), event -> consumer.accept(event, listener)));
    }

    public static <TCallback, TEvent extends Event & Cancellable> void bindCancelable(EventMapper<TCallback> mapper, EventBus<TEvent> bus, BiFunction<TEvent, TCallback, Boolean> consumer) {
        mapper.configureMapping((phase, listener) -> bus.addListener(mapPriority(phase), event -> consumer.apply(event, listener)));
    }

    public static <TCallback, TEvent extends Event> void bindFiltered(EventMapper<TCallback> mapper, EventBus<TEvent> bus, Predicate<TEvent> filter, BiConsumer<TEvent, TCallback> consumer) {
        mapper.configureMapping((phase, listener) -> bus.addListener(mapPriority(phase), event -> {
            if (filter.test(event)) {
                consumer.accept(event, listener);
            }
        }));
    }

    public static byte mapPriority(Identifier phase) {
        return PRIORITIES.getOrDefault(phase, Priority.NORMAL);
    }
}
