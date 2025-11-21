package net.blay09.mods.balm.fabric.platform.event.internal;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.callback.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.Objects;

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

    public static final Event<ItemCallback.Craft.After> ITEM_CRAFTED = EventFactory.createArrayBacked(ItemCallback.Craft.After.class, (listeners) -> (player, itemStack, craftMatrix) -> {
        for (final var listener : listeners) {
            listener.afterCraft(player, itemStack, craftMatrix);
        }
    });

    public static final Event<ItemCallback.Toss.Before> ITEM_TOSSED = EventFactory.createArrayBacked(ItemCallback.Toss.Before.class, (listeners) -> (player, itemStack) -> {
        for (final var listener : listeners) {
            if (!listener.allowToss(player, itemStack)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<CommandCallback.Before> COMMAND = EventFactory.createArrayBacked(CommandCallback.Before.class, (listeners) -> (parseResults) -> {
        for (final var listener : listeners) {
            if (!listener.allowCommand(parseResults)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<LivingEntityCallback.Damage.Before> LIVING_DAMAGE = EventFactory.createArrayBacked(LivingEntityCallback.Damage.Before.class, (listeners) -> (entity, damageSource, damageAmount) -> {
        float newDamageAmount = damageAmount;
        for (final var listener : listeners) {
            newDamageAmount = listener.computeDamage(entity, damageSource, newDamageAmount);
        }
        return newDamageAmount;
    });

    public static final Event<LivingEntityCallback.Fall.Before> LIVING_FALL = EventFactory.createArrayBacked(LivingEntityCallback.Fall.Before.class, (listeners) -> (entity, fallDamage) -> {
        float newDamage = fallDamage;
        for (final var listener : listeners) {
            newDamage = listener.computeFallDamage(entity, newDamage);
        }
        return newDamage;
    });

    public static final Event<LivingEntityCallback.Heal.Before> LIVING_HEAL = EventFactory.createArrayBacked(LivingEntityCallback.Heal.Before.class, (listeners) -> (entity, amount) -> {
        float newAmount = amount;
        for (final var listener : listeners) {
            newAmount = listener.computeHeal(entity, amount);
        }
        return newAmount;
    });

    public static final Event<CropCallback.Grow.Before> CROP_GROW_PRE = EventFactory.createArrayBacked(CropCallback.Grow.Before.class, (listeners) -> (level, pos, state) -> {
        for (final var listener : listeners) {
            final var result = Objects.requireNonNull(listener.beforeGrow(level, pos, state), () -> "CropCallback.Grow.Before.Result must not be null in " + listener.getClass().getName());
            if (result != CropCallback.Grow.Before.Result.DEFAULT) {
                return result;
            }
        }
        return CropCallback.Grow.Before.Result.DEFAULT;
    });

    public static final Event<CropCallback.Grow.After> CROP_GROW_POST = EventFactory.createArrayBacked(CropCallback.Grow.After.class, (listeners) -> (level, pos, state) -> {
        for (final var listener : listeners) {
            listener.afterGrow(level, pos, state);
        }
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
