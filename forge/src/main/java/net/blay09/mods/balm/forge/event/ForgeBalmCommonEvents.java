package net.blay09.mods.balm.forge.event;


import net.blay09.mods.balm.api.event.*;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartingEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ForgeBalmCommonEvents {

    public static void registerEvents(ForgeBalmEvents events) {
        events.registerTickEvent(TickType.Server, TickPhase.Start, (ServerTickHandler handler) -> TickEvent.ServerTickEvent.Pre.BUS.addListener((orig) -> handler.handle(ServerLifecycleHooks.getCurrentServer())));
        events.registerTickEvent(TickType.Server, TickPhase.End, (ServerTickHandler handler) -> TickEvent.ServerTickEvent.Post.BUS.addListener((orig) -> handler.handle(ServerLifecycleHooks.getCurrentServer())));
        events.registerTickEvent(TickType.ServerLevel, TickPhase.Start, (ServerLevelTickHandler handler) -> TickEvent.LevelTickEvent.Pre.BUS.addListener((orig) -> {
            if (orig.side == LogicalSide.SERVER) {
                handler.handle(orig.level);
            }
        }));
        events.registerTickEvent(TickType.ServerLevel, TickPhase.End, (ServerLevelTickHandler handler) -> TickEvent.LevelTickEvent.Post.BUS.addListener((orig) -> {
            if (orig.side == LogicalSide.SERVER) {
                handler.handle(orig.level);
            }
        }));

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.Start, (ServerPlayerTickHandler handler) -> TickEvent.PlayerTickEvent.Pre.BUS.addListener((orig) -> {
            if (orig.side == LogicalSide.SERVER) {
                handler.handle(((ServerPlayer) orig.player));
            }
        }));

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.End, (ServerPlayerTickHandler handler) -> TickEvent.PlayerTickEvent.Post.BUS.addListener((orig) -> {
            if (orig.side == LogicalSide.SERVER) {
                handler.handle(((ServerPlayer) orig.player));
            }
        }));

        events.registerTickEvent(TickType.ClientEntity, TickPhase.Start, (EntityTickHandler handler) -> LivingEvent.LivingTickEvent.BUS.addListener((orig) -> { // TODO unlike Fabric and NeoForge, only ticks for living entities
            if (orig.getEntity().level().isClientSide) {
                handler.handle(orig.getEntity());
            }
        }));

        events.registerTickEvent(TickType.ClientEntity, TickPhase.End, (EntityTickHandler handler) -> LivingEvent.LivingTickEvent.BUS.addListener((orig) -> { // TODO ticks at same time as START on Forge
            if (orig.getEntity().level().isClientSide) {
                handler.handle(orig.getEntity());
            }
        }));

        events.registerTickEvent(TickType.ServerEntity, TickPhase.Start, (EntityTickHandler handler) -> LivingEvent.LivingTickEvent.BUS.addListener((orig) -> { // TODO unlike Fabric and NeoForge, only ticks for living entities
            if (!orig.getEntity().level().isClientSide) {
                handler.handle(orig.getEntity());
            }
        }));

        events.registerTickEvent(TickType.ServerEntity, TickPhase.End, (EntityTickHandler handler) -> LivingEvent.LivingTickEvent.BUS.addListener((orig) -> { // TODO ticks at same time as START on Forge
            if (!orig.getEntity().level().isClientSide) {
                handler.handle(orig.getEntity());
            }
        }));

        events.registerEvent(ServerStartingEvent.class, priority -> net.minecraftforge.event.server.ServerAboutToStartEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ServerStartingEvent event = new ServerStartingEvent(orig.getServer());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ServerStartedEvent.class, priority -> net.minecraftforge.event.server.ServerStartedEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ServerStartedEvent event = new ServerStartedEvent(orig.getServer());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ServerStoppedEvent.class, priority -> net.minecraftforge.event.server.ServerStoppedEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ServerStoppedEvent event = new ServerStoppedEvent(orig.getServer());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(UseBlockEvent.class, priority -> PlayerInteractEvent.RightClickBlock.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final UseBlockEvent event = new UseBlockEvent(orig.getEntity(), orig.getLevel(), orig.getHand(), orig.getHitVec());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCancellationResult(event.getInteractionResult());
                return true;
            }
            return false;
        }));

        events.registerEvent(UseItemEvent.class, priority -> PlayerInteractEvent.RightClickItem.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final UseItemEvent event = new UseItemEvent(orig.getEntity(), orig.getLevel(), orig.getHand());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCancellationResult(event.getInteractionResult());
                return true;
            }
            return false;
        }));

        events.registerEvent(PlayerLoginEvent.class, priority -> PlayerEvent.PlayerLoggedInEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final PlayerLoginEvent event = new PlayerLoginEvent((ServerPlayer) orig.getEntity());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(PlayerLogoutEvent.class, priority -> PlayerEvent.PlayerLoggedOutEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final PlayerLogoutEvent event = new PlayerLogoutEvent((ServerPlayer) orig.getEntity());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(BreakBlockEvent.class, priority -> BlockEvent.BreakEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            BlockEntity blockEntity = orig.getLevel().getBlockEntity(orig.getPos());
            final BreakBlockEvent event = new BreakBlockEvent((Level) orig.getLevel(), orig.getPlayer(), orig.getPos(), orig.getState(), blockEntity);
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(PlayerRespawnEvent.class, priority -> PlayerEvent.PlayerRespawnEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final PlayerRespawnEvent event = new PlayerRespawnEvent(((ServerPlayer) orig.getEntity()), (ServerPlayer) orig.getEntity());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(LivingFallEvent.class, priority -> net.minecraftforge.event.entity.living.LivingFallEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final LivingFallEvent event = new LivingFallEvent(orig.getEntity());
            events.fireEventHandlers(priority, event);

            if (event.getFallDamageOverride() != null) {
                orig.setDamageMultiplier(0f);
                event.getEntity().hurt(event.getEntity().level().damageSources().fall(), event.getFallDamageOverride());
            }

            return event.isCanceled();
        }));

        events.registerEvent(LivingDamageEvent.class, priority -> net.minecraftforge.event.entity.living.LivingDamageEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final LivingDamageEvent event = new LivingDamageEvent(orig.getEntity(), orig.getSource(), orig.getAmount());
            events.fireEventHandlers(priority, event);
            orig.setAmount(event.getDamageAmount());
            return event.isCanceled();
        }));

        events.registerEvent(CropGrowEvent.Pre.class, priority -> BlockEvent.CropGrowEvent.Pre.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            if (orig.getLevel() instanceof Level level) {
                final CropGrowEvent.Pre event = new CropGrowEvent.Pre(level, orig.getPos(), orig.getState());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setResult(Result.DENY);
                }
            }
        }));

        events.registerEvent(CropGrowEvent.Post.class, priority -> BlockEvent.CropGrowEvent.Post.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            if (orig.getLevel() instanceof Level level) {
                final CropGrowEvent.Post event = new CropGrowEvent.Post(level, orig.getPos(), orig.getState());
                events.fireEventHandlers(priority, event);
            }
        }));

        events.registerEvent(ChunkTrackingEvent.Start.class, priority -> ChunkWatchEvent.Watch.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ChunkTrackingEvent.Start event = new ChunkTrackingEvent.Start(orig.getLevel(), orig.getPlayer(), orig.getPos());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ChunkTrackingEvent.Stop.class, priority -> ChunkWatchEvent.UnWatch.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ChunkTrackingEvent.Stop event = new ChunkTrackingEvent.Stop(orig.getLevel(), orig.getPlayer(), orig.getPos());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(TossItemEvent.class, priority -> ItemTossEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (ItemTossEvent orig) -> {
            final TossItemEvent event = new TossItemEvent(orig.getPlayer(), orig.getEntity().getItem());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(PlayerAttackEvent.class, priority -> AttackEntityEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final PlayerAttackEvent event = new PlayerAttackEvent(orig.getEntity(), orig.getTarget());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(LivingHealEvent.class, priority -> net.minecraftforge.event.entity.living.LivingHealEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final LivingHealEvent event = new LivingHealEvent(orig.getEntity(), orig.getAmount());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(PlayerChangedDimensionEvent.class, priority -> PlayerEvent.PlayerChangedDimensionEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final PlayerChangedDimensionEvent event = new PlayerChangedDimensionEvent((ServerPlayer) orig.getEntity(), orig.getFrom(), orig.getTo());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ItemCraftedEvent.class, priority -> PlayerEvent.ItemCraftedEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ItemCraftedEvent event = new ItemCraftedEvent(orig.getEntity(), orig.getCrafting(), orig.getInventory());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(CommandEvent.class, priority -> net.minecraftforge.event.CommandEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final CommandEvent event = new CommandEvent(orig.getParseResults());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(LivingDeathEvent.class, priority -> net.minecraftforge.event.entity.living.LivingDeathEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final LivingDeathEvent event = new LivingDeathEvent(orig.getEntity(), orig.getSource());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(EntityAddedEvent.class, priority -> EntityJoinLevelEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final EntityAddedEvent event = new EntityAddedEvent(orig.getEntity(), orig.getLevel());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ChunkLoadingEvent.Load.class, priority -> net.minecraftforge.event.level.ChunkEvent.Load.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ChunkLoadingEvent.Load event = new ChunkLoadingEvent.Load(orig.getLevel(), orig.getChunk());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ChunkLoadingEvent.Unload.class, priority -> net.minecraftforge.event.level.ChunkEvent.Unload.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ChunkLoadingEvent.Unload event = new ChunkLoadingEvent.Unload(orig.getLevel(), orig.getChunk());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(LevelLoadingEvent.Load.class, priority -> net.minecraftforge.event.level.LevelEvent.Load.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final LevelLoadingEvent.Load event = new LevelLoadingEvent.Load(orig.getLevel());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(LevelLoadingEvent.Unload.class, priority -> net.minecraftforge.event.level.LevelEvent.Unload.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final LevelLoadingEvent.Unload event = new LevelLoadingEvent.Unload(orig.getLevel());
            events.fireEventHandlers(priority, event);
        }));

    }

}
