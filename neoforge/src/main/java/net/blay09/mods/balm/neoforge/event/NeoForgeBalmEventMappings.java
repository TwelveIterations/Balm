package net.blay09.mods.balm.neoforge.event;

import net.blay09.mods.balm.event.BalmSupplementalEvents;
import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.event.EventPhases;
import net.blay09.mods.balm.event.callback.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
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
        ServerLifecycleCallback.RELOADING.setup(BalmSupplementalEvents.SERVER_RELOADING::register);
        ServerLifecycleCallback.RELOADED.setup(BalmSupplementalEvents.SERVER_RELOADED::register);

        bindCancelable(BlockCallback.DigSpeed.EVENT, PlayerEvent.BreakSpeed.class, (PlayerEvent.BreakSpeed event, BlockCallback.DigSpeed it) -> event.getPosition().map(pos -> {
            final var level = event.getEntity().level();
            final var speed = it.handle(level, pos, event.getState(), event.getEntity(), event.getNewSpeed());
            if (speed == -1f) {
                return true;
            }
            event.setNewSpeed(speed);
            return false;
        }).orElse(false));
        bindCancelable(BlockCallback.Break.EVENT, BlockEvent.BreakEvent.class, (event, it) -> {
            final var level = event.getLevel();
            final var blockEntity = level.getBlockEntity(event.getPos());
            return it.handle(level, event.getPos(), event.getState(), blockEntity, event.getPlayer()).shouldSkipDefault();
        });
        bindCancelable(BlockCallback.Use.EVENT, PlayerInteractEvent.RightClickBlock.class, (event, it) -> {
            final var result = it.handle(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            return result != InteractionResult.PASS;
        });

        bindCancelable(CommandCallback.EVENT, CommandEvent.class, (event, it) -> it.handle(event.getParseResults()).shouldSkipDefault());

        ConfigCallback.LOADED.setup(NeoForgeBalmSupplementalEvents.CONFIG_LOADED::register);
        ConfigCallback.RELOADED.setup(NeoForgeBalmSupplementalEvents.CONFIG_RELOADED::register);

        bindSimple(CropCallback.Grow.PRE, CropGrowEvent.Pre.class, (event, it) -> {
            if (it.handle(event.getLevel(), event.getPos(), event.getState()).shouldSkipDefault()) {
                event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
            }
        });
        bindSimple(CropCallback.Grow.POST, CropGrowEvent.Post.class, (event, it) -> it.handle(event.getLevel(), event.getPos(), event.getState()));

        bindSimple(EntityCallback.Add.EVENT, EntityJoinLevelEvent.class, (event, it) -> it.handle(event.getLevel(), event.getEntity()));

        bindSimple(CreativeModeTabCallback.BUILD_CONTENTS, BuildCreativeModeTabContentsEvent.class, (event, it) -> it.handle(event.getTab(), event));

        bindCancelable(ItemCallback.Use.EVENT, PlayerInteractEvent.RightClickItem.class, (event, it) -> {
            final var result = it.handle(event.getEntity(), event.getLevel(), event.getHand());
            return result != InteractionResult.PASS;
        });
        bindSimple(ItemCallback.Tooltip.EVENT, ItemTooltipEvent.class, (event, it) -> it.handle(event.getItemStack(), event.getToolTip(), event.getFlags()));
        bindSimple(ItemCallback.Craft.EVENT, PlayerEvent.ItemCraftedEvent.class, (event, it) -> it.handle(event.getEntity(), event.getCrafting(), event.getInventory()));
        bindCancelable(ItemCallback.Toss.EVENT, ItemTossEvent.class, (event, it) -> it.handle(event.getPlayer(), event.getEntity().getItem()).shouldSkipDefault());

        bindSimple(LevelCallback.LOAD, LevelEvent.Load.class, (event, it) -> it.handle(event.getLevel()));
        bindSimple(LevelCallback.UNLOAD, LevelEvent.Unload.class, (event, it) -> it.handle(event.getLevel()));
        bindSimple(LevelCallback.Chunk.LOAD, ChunkEvent.Load.class, (event, it) -> it.handle(event.getLevel(), event.getChunk(), event.getChunk().getPos()));
        bindSimple(LevelCallback.Chunk.UNLOAD, ChunkEvent.Unload.class, (event, it) -> it.handle(event.getLevel(), event.getChunk(), event.getChunk().getPos()));

        bindSimple(LivingEntityCallback.Heal.EVENT, LivingHealEvent.class, (event, it) -> it.handle(event.getEntity(), event.getAmount()));
        bindSimple(LivingEntityCallback.Fall.EVENT, LivingFallEvent.class, (event, it) -> it.handle(event.getEntity(), event.getDamageMultiplier()));
        bindSimple(LivingEntityCallback.Death.PRE, LivingDeathEvent.class, (event, it) -> it.handle(event.getEntity(), event.getSource()));
        // TODO no post event on Forge
        bindSimple(LivingEntityCallback.Death.POST, LivingDeathEvent.class, (event, it) -> it.handle(event.getEntity(), event.getSource()));
        bindSimple(LivingEntityCallback.Damage.EVENT, LivingDamageEvent.Pre.class, (event, it) -> it.handle(event.getEntity(), event.getSource(), event.getNewDamage()));

        bindSimple(PlayerCallback.Attack.EVENT, AttackEntityEvent.class, (event, it) -> it.handle(event.getEntity(), event.getTarget()));

        ServerPlayerCallback.CONNECTED.setup(NeoForgeBalmSupplementalEvents.SERVER_PLAYER_CONNECTED::register);
        bindSimple(ServerPlayerCallback.LOGIN, PlayerEvent.PlayerLoggedInEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.LOGOUT, PlayerEvent.PlayerLoggedOutEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.OpenMenu.EVENT, PlayerContainerEvent.Open.class, (event, it) -> it.handle((ServerPlayer) event.getEntity(), event.getContainer()));
        bindSimple(ServerPlayerCallback.DimensionChange.EVENT, PlayerEvent.PlayerChangedDimensionEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity(), event.getFrom(), event.getTo()));
        // TODO passing same entity twice currently
        bindSimple(ServerPlayerCallback.Respawn.EVENT, PlayerEvent.PlayerRespawnEvent.class, (event, it) -> it.handle((ServerPlayer) event.getEntity(), (ServerPlayer) event.getEntity()));
        bindSimple(ServerPlayerCallback.ChunkTracking.START, ChunkWatchEvent.Watch.class, (event, it) -> it.handle(event.getLevel(), event.getPlayer(), event.getPos()));
        bindSimple(ServerPlayerCallback.ChunkTracking.STOP, ChunkWatchEvent.UnWatch.class, (event, it) -> it.handle(event.getLevel(), event.getPlayer(), event.getPos()));
    }

    public static <TCallback, TEvent extends Event> void bindSimple(EventMapper<TCallback> mapper, Class<TEvent> eventClass, BiConsumer<TEvent, TCallback> consumer) {
        mapper.setup((phase, listener) -> NeoForge.EVENT_BUS.addListener(mapPriority(phase), eventClass, event -> consumer.accept(event, listener)));
    }

    public static <TCallback, TEvent extends Event & ICancellableEvent> void bindCancelable(EventMapper<TCallback> mapper, Class<TEvent> eventClass, BiFunction<TEvent, TCallback, Boolean> consumer) {
        mapper.setup((phase, listener) -> NeoForge.EVENT_BUS.addListener(mapPriority(phase), eventClass, event -> {
            if (consumer.apply(event, listener)) {
                event.setCanceled(true);
            }
        }));
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
