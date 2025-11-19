package net.blay09.mods.balm.fabric.event;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.EventHandling;
import net.blay09.mods.balm.event.callback.*;
import net.blay09.mods.balm.platform.event.callback.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class FabricBalmSupplementalEvents {
    public static final Event<ServerTickCallback.ServerPlayerTick> SERVER_PLAYER_TICK_PRE = EventFactory.createArrayBacked(ServerTickCallback.ServerPlayerTick.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ServerTickCallback.ServerPlayerTick> SERVER_PLAYER_TICK_POST = EventFactory.createArrayBacked(ServerTickCallback.ServerPlayerTick.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ServerTickCallback.ServerEntityTick> SERVER_ENTITY_TICK_PRE = EventFactory.createArrayBacked(ServerTickCallback.ServerEntityTick.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static final Event<ServerTickCallback.ServerEntityTick> SERVER_ENTITY_TICK_POST = EventFactory.createArrayBacked(ServerTickCallback.ServerEntityTick.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static final Event<ConfigCallback.Loaded> CONFIG_LOADED = EventFactory.createArrayBacked(ConfigCallback.Loaded.class, (listeners) -> (schema) -> {
        for (final var listener : listeners) {
            listener.handle(schema);
        }
    });

    public static final Event<ConfigCallback.Reloaded> CONFIG_RELOADED = EventFactory.createArrayBacked(ConfigCallback.Reloaded.class, (listeners) -> (schema) -> {
        for (final var listener : listeners) {
            listener.handle(schema);
        }
    });

    public static final Event<ServerPlayerCallback.OpenMenu> SERVER_PLAYER_OPEN_MENU = EventFactory.createArrayBacked(ServerPlayerCallback.OpenMenu.class, (listeners) -> (player, menu) -> {
        for (final var listener : listeners) {
            listener.handle(player, menu);
        }
    });

    public static final Event<ServerPlayerCallback.DimensionChange> SERVER_PLAYER_CHANGED_DIMENSION = EventFactory.createArrayBacked(ServerPlayerCallback.DimensionChange.class, (listeners) -> (player, from, to) -> {
        for (final var listener : listeners) {
            listener.handle(player, from, to);
        }
    });

    public static final Event<ServerPlayerCallback.ChunkTracking> SERVER_PLAYER_CHUNK_TRACKING_START = EventFactory.createArrayBacked(ServerPlayerCallback.ChunkTracking.class, (listeners) -> (level, player, chunkPos) -> {
        for (final var listener : listeners) {
            listener.handle(level, player, chunkPos);
        }
    });

    public static final Event<ServerPlayerCallback.ChunkTracking> SERVER_PLAYER_CHUNK_TRACKING_STOP = EventFactory.createArrayBacked(ServerPlayerCallback.ChunkTracking.class, (listeners) -> (level, player, chunkPos) -> {
        for (final var listener : listeners) {
            listener.handle(level, player, chunkPos);
        }
    });

    public static final Event<ServerPlayerCallback.Login> SERVER_PLAYER_LOGIN = EventFactory.createArrayBacked(ServerPlayerCallback.Login.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ItemCallback.Craft> ITEM_CRAFTED = EventFactory.createArrayBacked(ItemCallback.Craft.class, (listeners) -> (player, itemStack, craftMatrix) -> {
        for (final var listener : listeners) {
            listener.handle(player, itemStack, craftMatrix);
        }
    });

    public static final Event<ItemCallback.Toss> ITEM_TOSSED = EventFactory.createArrayBacked(ItemCallback.Toss.class, (listeners) -> (player, itemStack) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(player, itemStack));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<CommandCallback> COMMAND = EventFactory.createArrayBacked(CommandCallback.class, (listeners) -> (parseResults) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(parseResults));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<LivingEntityCallback.Damage> LIVING_DAMAGE = EventFactory.createArrayBacked(LivingEntityCallback.Damage.class, (listeners) -> (entity, damageSource, damageAmount) -> {
        float newDamageAmount = damageAmount;
        for (final var listener : listeners) {
            newDamageAmount = listener.handle(entity, damageSource, newDamageAmount);
        }
        return newDamageAmount;
    });

    public static final Event<LivingEntityCallback.Fall> LIVING_FALL = EventFactory.createArrayBacked(LivingEntityCallback.Fall.class, (listeners) -> (entity, fallDamage) -> {
        float newDamage = fallDamage;
        for (final var listener : listeners) {
            newDamage = listener.handle(entity, newDamage);
        }
        return newDamage;
    });

    public static final Event<LivingEntityCallback.Heal> LIVING_HEAL = EventFactory.createArrayBacked(LivingEntityCallback.Heal.class, (listeners) -> (entity, amount) -> {
        float newAmount = amount;
        for (final var listener : listeners) {
            newAmount = listener.handle(entity, amount);
        }
        return newAmount;
    });

    public static final Event<PlayerCallback.Attack> PLAYER_ATTACK = EventFactory.createArrayBacked(PlayerCallback.Attack.class, (listeners) -> (player, target) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(player, target));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<CropCallback.Grow> CROP_GROW_PRE = EventFactory.createArrayBacked(CropCallback.Grow.class, (listeners) -> (level, pos, state) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(level, pos, state));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<CropCallback.Grow> CROP_GROW_POST = EventFactory.createArrayBacked(CropCallback.Grow.class, (listeners) -> (level, pos, state) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(level, pos, state));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static void initialize() {
        ServerTickEvents.START_WORLD_TICK.register(level -> {
            if (SERVER_PLAYER_TICK_PRE.hasHandlers()) {
                for (final var player : level.players()) {
                    SERVER_PLAYER_TICK_PRE.invoker().handle(player);
                }
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            if (SERVER_PLAYER_TICK_POST.hasHandlers()) {
                for (final var player : level.players()) {
                    SERVER_PLAYER_TICK_POST.invoker().handle(player);
                }
            }
        });

        ServerTickEvents.START_WORLD_TICK.register(level -> {
            if (SERVER_ENTITY_TICK_PRE.hasHandlers()) {
                for (final var entity : level.getAllEntities()) {
                    SERVER_ENTITY_TICK_PRE.invoker().handle(entity);
                }
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            if (SERVER_ENTITY_TICK_POST.hasHandlers()) {
                for (final var entity : level.getAllEntities()) {
                    SERVER_ENTITY_TICK_POST.invoker().handle(entity);
                }
            }
        });
    }
}
