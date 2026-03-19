package net.blay09.mods.balm.fabric.platform.event.internal;

import net.blay09.mods.balm.platform.event.internal.BalmSupplementalEvents;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventPhases;
import net.blay09.mods.balm.platform.event.callback.*;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;

import java.util.Map;

public class FabricBalmEventMappings {
    private static final Map<Identifier, Identifier> PRIORITIES = Map.of(
            EventPhases.LOWEST, EventPhases.LOWEST,
            EventPhases.LOW, EventPhases.LOW,
            EventPhases.DEFAULT, net.fabricmc.fabric.api.event.Event.DEFAULT_PHASE,
            EventPhases.HIGH, EventPhases.HIGH,
            EventPhases.HIGHEST, EventPhases.HIGHEST
    );

    public static void bind() {
        ServerTickCallback.BEFORE.configureMapping((phase, it)
                -> ServerTickEvents.START_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.AFTER.configureMapping((phase, it)
                -> ServerTickEvents.END_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.ServerLevelTick.BEFORE.configureMapping((phase, it)
                -> ServerTickEvents.START_LEVEL_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.ServerLevelTick.AFTER.configureMapping((phase, it)
                -> ServerTickEvents.END_LEVEL_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.ServerPlayerTick.BEFORE.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_TICK_PRE::register);
        ServerTickCallback.ServerPlayerTick.AFTER.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_TICK_POST::register);
        ServerTickCallback.ServerEntityTick.BEFORE.configureMapping(FabricBalmSupplementalEvents.SERVER_ENTITY_TICK_PRE::register);
        ServerTickCallback.ServerEntityTick.AFTER.configureMapping(FabricBalmSupplementalEvents.SERVER_ENTITY_TICK_POST::register);

        ServerLifecycleCallback.Starting.EVENT.configureMapping((phase, it)
                -> ServerLifecycleEvents.SERVER_STARTING.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.Started.EVENT.configureMapping((phase, it)
                -> ServerLifecycleEvents.SERVER_STARTED.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.Stopping.EVENT.configureMapping((phase, it)
                -> ServerLifecycleEvents.SERVER_STOPPING.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.Stopped.EVENT.configureMapping((phase, it)
                -> ServerLifecycleEvents.SERVER_STOPPED.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.Reloading.EVENT.configureMapping(BalmSupplementalEvents.SERVER_RELOADING::register);
        ServerLifecycleCallback.Reloaded.EVENT.configureMapping(BalmSupplementalEvents.SERVER_RELOADED::register);

        ConfigCallback.Loaded.EVENT.configureMapping(BalmSupplementalEvents.CONFIG_LOADED::register);
        ConfigCallback.Reloaded.EVENT.configureMapping(BalmSupplementalEvents.CONFIG_RELOADED::register);

        ServerPlayerCallback.Join.EVENT.configureMapping((phase, it) -> ServerPlayerEvents.JOIN.register(mapPhase(phase), it::handle));
        ServerPlayerCallback.Leave.EVENT.configureMapping((phase, it) -> ServerPlayerEvents.LEAVE.register(mapPhase(phase), it::handle));
        ServerPlayerCallback.OpenMenu.EVENT.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_OPEN_MENU::register);
        ServerPlayerCallback.DimensionChange.EVENT.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_CHANGED_DIMENSION::register);
        ServerPlayerCallback.Respawn.EVENT.configureMapping((phase, it)
                -> ServerPlayerEvents.AFTER_RESPAWN.register(mapPhase(phase), (oldPlayer, newPlayer, alive) -> it.handle(oldPlayer, newPlayer)));
        ServerPlayerCallback.ChunkTracking.START.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_CHUNK_TRACKING_START::register);
        ServerPlayerCallback.ChunkTracking.STOP.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_CHUNK_TRACKING_STOP::register);

        LevelCallback.LOAD.configureMapping((phase, it)
                -> ServerLevelEvents.LOAD.register(mapPhase(phase), (server, world) -> it.handle(world)));
        LevelCallback.UNLOAD.configureMapping((phase, it)
                -> ServerLevelEvents.UNLOAD.register(mapPhase(phase), (server, world) -> it.handle(world)));
        LevelCallback.Chunk.LOAD.configureMapping((phase, it)
                -> ServerChunkEvents.CHUNK_LOAD.register(mapPhase(phase), (level, chunk, generated) -> it.handle(level, chunk, chunk.getPos())));
        LevelCallback.Chunk.UNLOAD.configureMapping((phase, it)
                -> ServerChunkEvents.CHUNK_UNLOAD.register(mapPhase(phase), (level, chunk) -> it.handle(level, chunk, chunk.getPos())));

        ItemCallback.Craft.After.EVENT.configureMapping(FabricBalmSupplementalEvents.ITEM_CRAFTED::register);
        ItemCallback.Toss.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.ITEM_TOSSED::register);
        ItemCallback.Use.EVENT.configureMapping((phase, it)
                -> UseItemCallback.EVENT.register(mapPhase(phase), (player, level, hand) -> mapInteractionResult(it.handle(player, level, hand))));
        ItemCallback.Tooltip.EVENT.configureMapping((phase, it)
                -> ItemTooltipCallback.EVENT.register(mapPhase(phase), (itemStack, context, flag, tooltip) -> it.handle(itemStack, tooltip, flag)));

        CommandCallback.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.COMMAND::register);

        EntityCallback.AddedToLevel.EVENT.configureMapping((phase, it)
                -> ServerEntityEvents.ENTITY_LOAD.register(mapPhase(phase), (entity, level) -> it.handle(level, entity)));
        EntityCallback.RemovedFromLevel.EVENT.configureMapping((phase, it)
                -> ServerEntityEvents.ENTITY_UNLOAD.register(mapPhase(phase), (entity, level) -> it.handle(level, entity)));
        EntityCallback.DimensionChange.BEFORE.configureMapping(FabricBalmSupplementalEvents.ENTITY_CHANGED_DIMENSION::register);

        LivingEntityCallback.Damage.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.LIVING_DAMAGE::register);
        LivingEntityCallback.Fall.Before.EVENT.configureMapping(BalmSupplementalEvents.LIVING_FALL::register);
        LivingEntityCallback.Heal.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.LIVING_HEAL::register);
        LivingEntityCallback.Death.Before.EVENT.configureMapping((phase, it)
                -> ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damage) -> it.allowDeath(livingEntity, damageSource)));

        LivingEntityCallback.MobEffectCallback.Apply.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.MOB_EFFECT_APPLY::register);
        LivingEntityCallback.MobEffectCallback.Add.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.MOB_EFFECT_ADD::register);
        LivingEntityCallback.MobEffectCallback.Remove.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.MOB_EFFECT_REMOVE::register);
        LivingEntityCallback.MobEffectCallback.Expire.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.MOB_EFFECT_EXPIRE::register);

        PlayerCallback.Attack.Before.EVENT.configureMapping((phase, it)
                -> AttackEntityCallback.EVENT.register(mapPhase(phase), (player, target, hand, entity, entityHitResult) -> !it.allowAttack(player, entity) ? InteractionResult.FAIL : InteractionResult.PASS));

        CropCallback.Grow.Before.EVENT.configureMapping(FabricBalmSupplementalEvents.CROP_GROW_PRE::register);
        CropCallback.Grow.After.EVENT.configureMapping(FabricBalmSupplementalEvents.CROP_GROW_POST::register);

        BlockCallback.Use.EVENT.configureMapping((phase, it)
                -> UseBlockCallback.EVENT.register(mapPhase(phase), (player, level, hand, hitResult) -> mapInteractionResult(it.handle(player, level, hand, hitResult))));
        BlockCallback.DigSpeed.EVENT.configureMapping(BalmSupplementalEvents.BLOCK_DIG_SPEED::register);
        BlockCallback.Break.Before.EVENT.configureMapping((phase, it)
                -> PlayerBlockBreakEvents.BEFORE.register(mapPhase(phase), (world, player, pos, state, blockEntity) -> it.allowBreak(world, pos, state, blockEntity, player)));

        CreativeModeTabCallback.BuildContents.EVENT.configureMapping((phase, it)
                -> CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register(mapPhase(phase), it::handle));
    }

    private static InteractionResult mapInteractionResult(InteractionEventResult result) {
        return result.interactionResult().orElse(InteractionResult.PASS);
    }

    public static Identifier mapPhase(Identifier phase) {
        return PRIORITIES.getOrDefault(phase, Event.DEFAULT_PHASE);
    }
}
