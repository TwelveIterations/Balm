package net.blay09.mods.balm.neoforge.platform.event.internal;

import net.blay09.mods.balm.platform.event.internal.BalmSupplementalEvents;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.blay09.mods.balm.platform.event.EventPhases;
import net.blay09.mods.balm.platform.event.callback.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
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
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class NeoForgeBalmEventMappings {
    private static final Map<Identifier, EventPriority> PRIORITIES = Map.of(
            EventPhases.LOWEST, EventPriority.LOWEST,
            EventPhases.LOW, EventPriority.LOW,
            EventPhases.DEFAULT, EventPriority.NORMAL,
            EventPhases.HIGH, EventPriority.HIGH,
            EventPhases.HIGHEST, EventPriority.HIGHEST
    );

    public static void bind() {
        bindSimple(ServerTickCallback.BEFORE, ServerTickEvent.Pre.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerTickCallback.AFTER, ServerTickEvent.Post.class, (event, it) -> it.handle(event.getServer()));
        bindFiltered(ServerTickCallback.ServerLevelTick.BEFORE, LevelTickEvent.Pre.class, event -> !event.getLevel().isClientSide(), (event, it) -> it.handle((ServerLevel) event.getLevel()));
        bindFiltered(ServerTickCallback.ServerLevelTick.AFTER, LevelTickEvent.Post.class, event -> !event.getLevel().isClientSide(), (event, it) -> it.handle((ServerLevel) event.getLevel()));
        bindFiltered(ServerTickCallback.ServerPlayerTick.BEFORE, PlayerTickEvent.Pre.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindFiltered(ServerTickCallback.ServerPlayerTick.AFTER, PlayerTickEvent.Post.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindFiltered(ServerTickCallback.ServerEntityTick.BEFORE, EntityTickEvent.Pre.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));
        bindFiltered(ServerTickCallback.ServerEntityTick.AFTER, EntityTickEvent.Post.class, event -> !event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));

        bindSimple(ServerLifecycleCallback.Starting.EVENT, ServerAboutToStartEvent.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.Started.EVENT, ServerStartedEvent.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.Stopping.EVENT, ServerStoppingEvent.class, (event, it) -> it.handle(event.getServer()));
        bindSimple(ServerLifecycleCallback.Stopped.EVENT, ServerStoppedEvent.class, (event, it) -> it.handle(event.getServer()));
        ServerLifecycleCallback.Reloading.EVENT.configureMapping(BalmSupplementalEvents.SERVER_RELOADING::register);
        ServerLifecycleCallback.Reloaded.EVENT.configureMapping(BalmSupplementalEvents.SERVER_RELOADED::register);

        bindCancelable(BlockCallback.DigSpeed.EVENT, PlayerEvent.BreakSpeed.class, (PlayerEvent.BreakSpeed event, BlockCallback.DigSpeed it) -> event.getPosition().map(pos -> {
            final var level = event.getEntity().level();
            final var speed = it.computeDigSpeed(level, pos, event.getState(), event.getEntity(), event.getNewSpeed());
            if (speed == -1f) {
                return true;
            }
            event.setNewSpeed(speed);
            return false;
        }).orElse(false));
        bindCancelable(BlockCallback.Break.Before.EVENT, BreakBlockEvent.class, (event, it) -> {
            final var level = event.getLevel();
            final var blockEntity = level.getBlockEntity(event.getPos());
            return !it.allowBreak(level, event.getPos(), event.getState(), blockEntity, event.getPlayer());
        });
        bindCancelable(BlockCallback.Use.EVENT, PlayerInteractEvent.RightClickBlock.class, (event, it) -> {
            final var result = it.handle(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            result.interactionResult().ifPresent(event::setCancellationResult);
            return result.interactionResult().isPresent();
        });

        bindCancelable(CommandCallback.Before.EVENT, CommandEvent.class, (event, it) -> !it.allowCommand(event.getParseResults()));

        ConfigCallback.Loaded.EVENT.configureMapping(BalmSupplementalEvents.CONFIG_LOADED::register);
        ConfigCallback.Reloaded.EVENT.configureMapping(BalmSupplementalEvents.CONFIG_RELOADED::register);

        bindSimple(CropCallback.Grow.Before.EVENT, CropGrowEvent.Pre.class, (event, it) -> {
            final var result = it.beforeGrow(event.getLevel(), event.getPos(), event.getState());
            event.setResult(switch (result) {
                case DO_NOT_GROW -> CropGrowEvent.Pre.Result.DO_NOT_GROW;
                case GROW -> CropGrowEvent.Pre.Result.GROW;
                default -> CropGrowEvent.Pre.Result.DEFAULT;
            });
        });
        bindSimple(CropCallback.Grow.After.EVENT, CropGrowEvent.Post.class, (event, it) -> it.afterGrow(event.getLevel(), event.getPos(), event.getState()));

        bindSimple(EntityCallback.AddedToLevel.EVENT, EntityJoinLevelEvent.class, (event, it) -> it.handle(event.getLevel(), event.getEntity()));
        bindSimple(EntityCallback.RemovedFromLevel.EVENT, EntityLeaveLevelEvent.class, (event, it) -> it.handle(event.getLevel(), event.getEntity()));
        bindCancelable(EntityCallback.DimensionChange.BEFORE, EntityTravelToDimensionEvent.class, (event, it) -> !it.allowDimensionChange(event.getEntity(), event.getEntity().level().dimension(), event.getDimension()));

        bindSimple(CreativeModeTabCallback.BuildContents.EVENT, BuildCreativeModeTabContentsEvent.class, (event, it) -> it.handle(event.getTab(), event));

        bindCancelable(ItemCallback.Use.EVENT, PlayerInteractEvent.RightClickItem.class, (event, it) -> {
            final var result = it.handle(event.getEntity(), event.getLevel(), event.getHand());
            result.interactionResult().ifPresent(event::setCancellationResult);
            return result.interactionResult().isPresent();
        });
        bindSimple(ItemCallback.Tooltip.EVENT, ItemTooltipEvent.class, (event, it) -> it.handle(event.getItemStack(), event.getToolTip(), event.getFlags()));
        bindSimple(ItemCallback.Craft.After.EVENT, PlayerEvent.ItemCraftedEvent.class, (event, it) -> it.afterCraft(event.getEntity(), event.getCrafting(), event.getInventory()));
        bindCancelable(ItemCallback.Toss.Before.EVENT, ItemTossEvent.class, (event, it) -> !it.allowToss(event.getPlayer(), event.getEntity().getItem()));

        bindSimple(LevelCallback.LOAD, LevelEvent.Load.class, (event, it) -> it.handle(event.getLevel()));
        bindSimple(LevelCallback.UNLOAD, LevelEvent.Unload.class, (event, it) -> it.handle(event.getLevel()));
        bindSimple(LevelCallback.Chunk.LOAD, ChunkEvent.Load.class, (event, it) -> it.handle(event.getLevel(), event.getChunk(), event.getChunk().getPos()));
        bindSimple(LevelCallback.Chunk.UNLOAD, ChunkEvent.Unload.class, (event, it) -> it.handle(event.getLevel(), event.getChunk(), event.getChunk().getPos()));

        bindSimple(LivingEntityCallback.Heal.Before.EVENT, LivingHealEvent.class, (event, it) -> event.setAmount(it.computeHeal(event.getEntity(), event.getAmount())));
        LivingEntityCallback.Fall.Before.EVENT.configureMapping(BalmSupplementalEvents.LIVING_FALL::register);
        bindCancelable(LivingEntityCallback.Death.Before.EVENT, LivingDeathEvent.class, (event, it) -> !it.allowDeath(event.getEntity(), event.getSource()));
        bindSimple(LivingEntityCallback.Damage.Before.EVENT, LivingDamageEvent.Pre.class, (event, it) -> it.computeDamage(event.getEntity(), event.getSource(), event.getNewDamage()));

        bindSimple(LivingEntityCallback.MobEffectCallback.Apply.Before.EVENT, MobEffectEvent.Applicable.class, (event, it) -> it.allowApply(event.getEntity(), event.getEffectInstance(), event.getEffectSource()));
        bindSimple(LivingEntityCallback.MobEffectCallback.Add.Before.EVENT, MobEffectEvent.Added.class, (event, it) -> it.effectAdded(event.getEntity(), event.getEffectInstance(), event.getOldEffectInstance(), event.getEffectSource()));
        bindCancelable(LivingEntityCallback.MobEffectCallback.Remove.Before.EVENT, MobEffectEvent.Remove.class, (event, it) -> !it.allowRemove(event.getEntity(), event.getEffect(), event.getEffectInstance()));
        bindCancelable(LivingEntityCallback.MobEffectCallback.Expire.Before.EVENT, MobEffectEvent.Expired.class, (event, it) -> !it.allowExpire(event.getEntity(), event.getEffectInstance()));

        bindCancelable(PlayerCallback.Attack.Before.EVENT, AttackEntityEvent.class, (event, it) -> !it.allowAttack(event.getEntity(), event.getTarget()));

        bindSimple(ServerPlayerCallback.Join.EVENT, PlayerEvent.PlayerLoggedInEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.Leave.EVENT, PlayerEvent.PlayerLoggedOutEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.OpenMenu.EVENT, PlayerContainerEvent.Open.class, (event, it) -> it.handle((ServerPlayer) event.getEntity(), event.getContainer()));
        bindSimple(ServerPlayerCallback.DimensionChange.EVENT, PlayerEvent.PlayerChangedDimensionEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity(), event.getFrom(), event.getTo()));
        // TODO passing same entity twice currently
        bindSimple(ServerPlayerCallback.Respawn.EVENT, PlayerEvent.PlayerRespawnEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity(), (ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.ChunkTracking.START, ChunkWatchEvent.Watch.class, (event, it) -> it.handle(event.getLevel(), event.getPlayer(), event.getPos()));
        bindSimple(ServerPlayerCallback.ChunkTracking.STOP, ChunkWatchEvent.UnWatch.class, (event, it) -> it.handle(event.getLevel(), event.getPlayer(), event.getPos()));
    }

    public static <TCallback, TEvent extends Event> void bindSimple(EventMapper<TCallback> mapper, Class<TEvent> eventClass, BiConsumer<TEvent, TCallback> consumer) {
        mapper.configureMapping((phase, listener) -> NeoForge.EVENT_BUS.addListener(mapPriority(phase), eventClass, event -> consumer.accept(event, listener)));
    }

    public static <TCallback, TEvent extends Event & ICancellableEvent> void bindCancelable(EventMapper<TCallback> mapper, Class<TEvent> eventClass, BiFunction<TEvent, TCallback, Boolean> consumer) {
        mapper.configureMapping((phase, listener) -> NeoForge.EVENT_BUS.addListener(mapPriority(phase), eventClass, event -> {
            if (consumer.apply(event, listener)) {
                event.setCanceled(true);
            }
        }));
    }

    public static <TCallback, TEvent extends Event> void bindFiltered(EventMapper<TCallback> mapper, Class<TEvent> eventClass, Predicate<TEvent> filter, BiConsumer<TEvent, TCallback> consumer) {
        mapper.configureMapping((phase, listener) -> NeoForge.EVENT_BUS.addListener(mapPriority(phase), eventClass, event -> {
            if (filter.test(event)) {
                consumer.accept(event, listener);
            }
        }));
    }

    public static EventPriority mapPriority(Identifier phase) {
        return PRIORITIES.getOrDefault(phase, EventPriority.NORMAL);
    }
}
