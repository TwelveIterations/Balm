package net.blay09.mods.balm.neoforge.event;


import net.blay09.mods.balm.api.event.*;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartingEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.blay09.mods.balm.world.item.BuildCreativeModeTabContentsEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class NeoForgeBalmCommonEvents {

    public static void registerEvents(NeoForgeBalmEvents events) {
        events.registerTickEvent(TickType.Server, TickPhase.Start, (ServerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((ServerTickEvent.Pre orig) -> {
                handler.handle(ServerLifecycleHooks.getCurrentServer());
            });
        });
        events.registerTickEvent(TickType.Server, TickPhase.End, (ServerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((ServerTickEvent orig) -> {
                handler.handle(ServerLifecycleHooks.getCurrentServer());
            });
        });
        events.registerTickEvent(TickType.ServerLevel, TickPhase.Start, (ServerLevelTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((LevelTickEvent.Pre orig) -> {
                if (orig.getLevel() instanceof ServerLevel serverLevel) {
                    handler.handle(serverLevel);
                }
            });
        });
        events.registerTickEvent(TickType.ServerLevel, TickPhase.End, (ServerLevelTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((LevelTickEvent.Post orig) -> {
                if (orig.getLevel() instanceof ServerLevel serverLevel) {
                    handler.handle(serverLevel);
                }
            });
        });

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.Start, (ServerPlayerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((PlayerTickEvent.Pre orig) -> {
                if (orig.getEntity() instanceof ServerPlayer serverPlayer) {
                    handler.handle(serverPlayer);
                }
            });
        });

        events.registerTickEvent(TickType.ServerPlayer, TickPhase.End, (ServerPlayerTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((PlayerTickEvent.Post orig) -> {
                if (orig.getEntity() instanceof ServerPlayer serverPlayer) {
                    handler.handle(serverPlayer);
                }
            });
        });

        events.registerTickEvent(TickType.ServerEntity, TickPhase.Start, (EntityTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((EntityTickEvent.Pre orig) -> {
                handler.handle(orig.getEntity());
            });
        });

        events.registerTickEvent(TickType.ServerEntity, TickPhase.End, (EntityTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((EntityTickEvent.Post orig) -> {
                handler.handle(orig.getEntity());
            });
        });

        events.registerEvent(ServerStartingEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.server.ServerAboutToStartEvent orig) -> {
                final ServerStartingEvent event = new ServerStartingEvent(orig.getServer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ServerStartedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.server.ServerStartedEvent orig) -> {
                final ServerStartedEvent event = new ServerStartedEvent(orig.getServer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ServerStoppedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.server.ServerStoppedEvent orig) -> {
                final ServerStoppedEvent event = new ServerStoppedEvent(orig.getServer());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(UseBlockEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerInteractEvent.RightClickBlock orig) -> {
                final UseBlockEvent event = new UseBlockEvent(orig.getEntity(), orig.getLevel(), orig.getHand(), orig.getHitVec());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCancellationResult(event.getInteractionResult());
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(UseItemEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerInteractEvent.RightClickItem orig) -> {
                final UseItemEvent event = new UseItemEvent(orig.getEntity(), orig.getLevel(), orig.getHand());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCancellationResult(event.getInteractionResult());
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerLoginEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerLoggedInEvent orig) -> {
                final PlayerLoginEvent event = new PlayerLoginEvent((ServerPlayer) orig.getEntity());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(PlayerLogoutEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerLoggedOutEvent orig) -> {
                final PlayerLogoutEvent event = new PlayerLogoutEvent((ServerPlayer) orig.getEntity());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(BreakBlockEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (BlockEvent.BreakEvent orig) -> {
                BlockEntity blockEntity = orig.getLevel().getBlockEntity(orig.getPos());
                final BreakBlockEvent event = new BreakBlockEvent((Level) orig.getLevel(), orig.getPlayer(), orig.getPos(), orig.getState(), blockEntity);
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerRespawnEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerRespawnEvent orig) -> {
                final PlayerRespawnEvent event = new PlayerRespawnEvent(((ServerPlayer) orig.getEntity()), (ServerPlayer) orig.getEntity());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(LivingFallEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.living.LivingFallEvent orig) -> {
                final LivingFallEvent event = new LivingFallEvent(orig.getEntity());
                events.fireEventHandlers(priority, event);

                if (event.getFallDamageOverride() != null) {
                    orig.setDamageMultiplier(0f);
                    event.getEntity().hurt(event.getEntity().level().damageSources().fall(), event.getFallDamageOverride());
                }

                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(LivingDamageEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre orig) -> {
                final var damageContainer = orig.getContainer();
                final LivingDamageEvent event = new LivingDamageEvent(orig.getEntity(), damageContainer.getSource(), damageContainer.getNewDamage());
                events.fireEventHandlers(priority, event);
                damageContainer.setNewDamage(event.getDamageAmount());
                if (event.isCanceled()) {
                    orig.getContainer().setNewDamage(0);
                }
            });
        });

        events.registerEvent(CropGrowEvent.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.level.block.CropGrowEvent.Pre orig) -> {
                if (orig.getLevel() instanceof Level level) {
                    final CropGrowEvent.Pre event = new CropGrowEvent.Pre(level, orig.getPos(), orig.getState());
                    events.fireEventHandlers(priority, event);
                    if (event.isCanceled()) {
                        orig.setResult(net.neoforged.neoforge.event.level.block.CropGrowEvent.Pre.Result.DO_NOT_GROW);
                    }
                }
            });
        });

        events.registerEvent(CropGrowEvent.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.level.block.CropGrowEvent.Post orig) -> {
                if (orig.getLevel() instanceof Level level) {
                    final CropGrowEvent.Post event = new CropGrowEvent.Post(level, orig.getPos(), orig.getState());
                    events.fireEventHandlers(priority, event);
                }
            });
        });

        events.registerEvent(ChunkTrackingEvent.Start.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ChunkWatchEvent.Watch orig) -> {
                final ChunkTrackingEvent.Start event = new ChunkTrackingEvent.Start(orig.getLevel(), orig.getPlayer(), orig.getPos());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ChunkTrackingEvent.Stop.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ChunkWatchEvent.UnWatch orig) -> {
                final ChunkTrackingEvent.Stop event = new ChunkTrackingEvent.Stop(orig.getLevel(), orig.getPlayer(), orig.getPos());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(TossItemEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ItemTossEvent orig) -> {
                final TossItemEvent event = new TossItemEvent(orig.getPlayer(), orig.getEntity().getItem());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerAttackEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (AttackEntityEvent orig) -> {
                final PlayerAttackEvent event = new PlayerAttackEvent(orig.getEntity(), orig.getTarget());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(LivingHealEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.living.LivingHealEvent orig) -> {
                final LivingHealEvent event = new LivingHealEvent(orig.getEntity(), orig.getAmount());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(PlayerChangedDimensionEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.PlayerChangedDimensionEvent orig) -> {
                final PlayerChangedDimensionEvent event = new PlayerChangedDimensionEvent((ServerPlayer) orig.getEntity(), orig.getFrom(), orig.getTo());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ItemCraftedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (PlayerEvent.ItemCraftedEvent orig) -> {
                final ItemCraftedEvent event = new ItemCraftedEvent(orig.getEntity(), orig.getCrafting(), orig.getInventory());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(CommandEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.CommandEvent orig) -> {
                final CommandEvent event = new CommandEvent(orig.getParseResults());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(LivingDeathEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.living.LivingDeathEvent orig) -> {
                final LivingDeathEvent event = new LivingDeathEvent(orig.getEntity(), orig.getSource());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(EntityAddedEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (EntityJoinLevelEvent orig) -> {
                final EntityAddedEvent event = new EntityAddedEvent(orig.getEntity(), orig.getLevel());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(BuildCreativeModeTabContentsEvent.class, priority -> NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent orig) -> {
            final var event = new BuildCreativeModeTabContentsEvent(orig.getTab(), orig);
            events.fireEventHandlers(priority, event);
        }));
    }

}
