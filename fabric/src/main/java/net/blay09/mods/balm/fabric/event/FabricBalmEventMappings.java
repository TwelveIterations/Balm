package net.blay09.mods.balm.fabric.event;

import net.blay09.mods.balm.event.BalmSupplementalEvents;
import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventPhases;
import net.blay09.mods.balm.event.callback.*;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class FabricBalmEventMappings {
    private static final Map<ResourceLocation, ResourceLocation> PRIORITIES = Map.of(
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
                -> ServerTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.ServerLevelTick.AFTER.configureMapping((phase, it)
                -> ServerTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));
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

        ConfigCallback.Loaded.EVENT.configureMapping(FabricBalmSupplementalEvents.CONFIG_LOADED::register);
        ConfigCallback.Reloaded.EVENT.configureMapping(FabricBalmSupplementalEvents.CONFIG_RELOADED::register);

        ServerPlayerCallback.Connected.EVENT.configureMapping((phase, it)
                -> ServerPlayConnectionEvents.JOIN.register(mapPhase(phase), (handler, sender, server) -> it.handle(handler.player)));
        ServerPlayerCallback.Login.EVENT.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_LOGIN::register);
        ServerPlayerCallback.Logout.EVENT.configureMapping((phase, it)
                -> ServerPlayConnectionEvents.DISCONNECT.register(mapPhase(phase), (handler, server) -> it.handle(handler.player)));
        ServerPlayerCallback.OpenMenu.EVENT.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_OPEN_MENU::register);
        ServerPlayerCallback.DimensionChange.EVENT.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_CHANGED_DIMENSION::register);
        ServerPlayerCallback.Respawn.EVENT.configureMapping((phase, it)
                -> ServerPlayerEvents.AFTER_RESPAWN.register(mapPhase(phase), (oldPlayer, newPlayer, alive) -> it.handle(oldPlayer, newPlayer)));
        ServerPlayerCallback.ChunkTracking.START.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_CHUNK_TRACKING_START::register);
        ServerPlayerCallback.ChunkTracking.STOP.configureMapping(FabricBalmSupplementalEvents.SERVER_PLAYER_CHUNK_TRACKING_STOP::register);

        LevelCallback.LOAD.configureMapping((phase, it)
                -> ServerWorldEvents.LOAD.register(mapPhase(phase), (server, world) -> it.handle(world)));
        LevelCallback.UNLOAD.configureMapping((phase, it)
                -> ServerWorldEvents.UNLOAD.register(mapPhase(phase), (server, world) -> it.handle(world)));
        LevelCallback.Chunk.LOAD.configureMapping((phase, it)
                -> ServerChunkEvents.CHUNK_LOAD.register(mapPhase(phase), (level, chunk) -> it.handle(level, chunk, chunk.getPos())));
        LevelCallback.Chunk.UNLOAD.configureMapping((phase, it)
                -> ServerChunkEvents.CHUNK_UNLOAD.register(mapPhase(phase), (level, chunk) -> it.handle(level, chunk, chunk.getPos())));

        ItemCallback.Craft.EVENT.configureMapping(FabricBalmSupplementalEvents.ITEM_CRAFTED::register);
        ItemCallback.Toss.EVENT.configureMapping(FabricBalmSupplementalEvents.ITEM_TOSSED::register);
        ItemCallback.Use.EVENT.configureMapping((phase, it)
                -> UseItemCallback.EVENT.register(mapPhase(phase), it::handle));
        ItemCallback.Tooltip.EVENT.configureMapping((phase, it)
                -> ItemTooltipCallback.EVENT.register(mapPhase(phase), (itemStack, context, flag, tooltip) -> it.handle(itemStack, tooltip, flag)));

        CommandCallback.EVENT.configureMapping(FabricBalmSupplementalEvents.COMMAND::register);

        EntityCallback.Add.EVENT.configureMapping((phase, it)
                -> ServerEntityEvents.ENTITY_LOAD.register(mapPhase(phase), (entity, level) -> it.handle(level, entity)));

        LivingEntityCallback.Damage.EVENT.configureMapping(FabricBalmSupplementalEvents.LIVING_DAMAGE::register);
        LivingEntityCallback.Fall.EVENT.configureMapping(FabricBalmSupplementalEvents.LIVING_FALL::register);
        LivingEntityCallback.Heal.EVENT.configureMapping(FabricBalmSupplementalEvents.LIVING_HEAL::register);
        LivingEntityCallback.Death.BEFORE.configureMapping((phase, it)
                -> ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damage) -> it.handle(livingEntity, damageSource).shouldSkipDefault()));
        LivingEntityCallback.Death.AFTER.configureMapping((phase, it)
                -> ServerLivingEntityEvents.AFTER_DEATH.register(it::handle));

        PlayerCallback.Attack.EVENT.configureMapping(FabricBalmSupplementalEvents.PLAYER_ATTACK::register);

        CropCallback.Grow.BEFORE.configureMapping(FabricBalmSupplementalEvents.CROP_GROW_PRE::register);
        CropCallback.Grow.AFTER.configureMapping(FabricBalmSupplementalEvents.CROP_GROW_POST::register);

        BlockCallback.Use.EVENT.configureMapping((phase, it)
                -> UseBlockCallback.EVENT.register(mapPhase(phase), it::handle));
        BlockCallback.DigSpeed.EVENT.configureMapping(BalmSupplementalEvents.BLOCK_DIG_SPEED::register);
        BlockCallback.Break.EVENT.configureMapping((phase, it)
                -> PlayerBlockBreakEvents.BEFORE.register(mapPhase(phase), (world, player, pos, state, blockEntity) -> !it.handle(world, pos, state, blockEntity, player).shouldSkipDefault()));

        CreativeModeTabCallback.BuildContents.EVENT.configureMapping((phase, it)
                -> ItemGroupEvents.MODIFY_ENTRIES_ALL.register(mapPhase(phase), it::handle));
    }

    public static ResourceLocation mapPhase(ResourceLocation phase) {
        return PRIORITIES.getOrDefault(phase, Event.DEFAULT_PHASE);
    }
}
