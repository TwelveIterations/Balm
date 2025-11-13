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
        ServerTickCallback.PRE.setup((phase, it)
                -> ServerTickEvents.START_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.POST.setup((phase, it)
                -> ServerTickEvents.END_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.Level.PRE.setup((phase, it)
                -> ServerTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.Level.POST.setup((phase, it)
                -> ServerTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.Player.PRE.setup(FabricBalmSupplementalEvents.SERVER_PLAYER_TICK_PRE::register);
        ServerTickCallback.Player.POST.setup(FabricBalmSupplementalEvents.SERVER_PLAYER_TICK_POST::register);
        ServerTickCallback.Entity.PRE.setup(FabricBalmSupplementalEvents.SERVER_ENTITY_TICK_PRE::register);
        ServerTickCallback.Entity.POST.setup(FabricBalmSupplementalEvents.SERVER_ENTITY_TICK_POST::register);

        ServerLifecycleCallback.STARTING.setup((phase, it)
                -> ServerLifecycleEvents.SERVER_STARTING.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.STARTED.setup((phase, it)
                -> ServerLifecycleEvents.SERVER_STARTED.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.STOPPING.setup((phase, it)
                -> ServerLifecycleEvents.SERVER_STOPPING.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.STOPPED.setup((phase, it)
                -> ServerLifecycleEvents.SERVER_STOPPED.register(mapPhase(phase), it::handle));
        ServerLifecycleCallback.RELOADING.setup(BalmSupplementalEvents.SERVER_RELOADING::register);
        ServerLifecycleCallback.RELOADED.setup(BalmSupplementalEvents.SERVER_RELOADED::register);

        ConfigCallback.LOADED.setup(FabricBalmSupplementalEvents.CONFIG_LOADED::register);
        ConfigCallback.RELOADED.setup(FabricBalmSupplementalEvents.CONFIG_RELOADED::register);

        ServerPlayerCallback.CONNECTED.setup((phase, it)
                -> ServerPlayConnectionEvents.JOIN.register(mapPhase(phase), (handler, sender, server) -> it.handle(handler.player)));
        ServerPlayerCallback.LOGIN.setup(FabricBalmSupplementalEvents.SERVER_PLAYER_LOGIN::register);
        ServerPlayerCallback.LOGOUT.setup((phase, it)
                -> ServerPlayConnectionEvents.DISCONNECT.register(mapPhase(phase), (handler, server) -> it.handle(handler.player)));
        ServerPlayerCallback.OpenMenu.EVENT.setup(FabricBalmSupplementalEvents.SERVER_PLAYER_OPEN_MENU::register);
        ServerPlayerCallback.DimensionChange.EVENT.setup(FabricBalmSupplementalEvents.SERVER_PLAYER_CHANGED_DIMENSION::register);
        ServerPlayerCallback.Respawn.EVENT.setup((phase, it)
                -> ServerPlayerEvents.AFTER_RESPAWN.register(mapPhase(phase), (oldPlayer, newPlayer, alive) -> it.handle(oldPlayer, newPlayer)));
        ServerPlayerCallback.ChunkTracking.START.setup(FabricBalmSupplementalEvents.SERVER_PLAYER_CHUNK_TRACKING_START::register);
        ServerPlayerCallback.ChunkTracking.STOP.setup(FabricBalmSupplementalEvents.SERVER_PLAYER_CHUNK_TRACKING_STOP::register);

        LevelCallback.LOAD.setup((phase, it)
                -> ServerWorldEvents.LOAD.register(mapPhase(phase), (server, world) -> it.handle(world)));
        LevelCallback.UNLOAD.setup((phase, it)
                -> ServerWorldEvents.UNLOAD.register(mapPhase(phase), (server, world) -> it.handle(world)));
        LevelCallback.Chunk.LOAD.setup((phase, it)
                -> ServerChunkEvents.CHUNK_LOAD.register(mapPhase(phase), (level, chunk) -> it.handle(level, chunk, chunk.getPos())));
        LevelCallback.Chunk.UNLOAD.setup((phase, it)
                -> ServerChunkEvents.CHUNK_UNLOAD.register(mapPhase(phase), (level, chunk) -> it.handle(level, chunk, chunk.getPos())));

        ItemCallback.Craft.EVENT.setup(FabricBalmSupplementalEvents.ITEM_CRAFTED::register);
        ItemCallback.Toss.EVENT.setup(FabricBalmSupplementalEvents.ITEM_TOSSED::register);
        ItemCallback.Use.EVENT.setup((phase, it)
                -> UseItemCallback.EVENT.register(mapPhase(phase), it::handle));
        ItemCallback.Tooltip.EVENT.setup((phase, it)
                -> ItemTooltipCallback.EVENT.register(mapPhase(phase), (itemStack, context, flag, tooltip) -> it.handle(itemStack, tooltip, flag)));

        CommandCallback.EVENT.setup(FabricBalmSupplementalEvents.COMMAND::register);

        EntityCallback.Add.EVENT.setup((phase, it)
                -> ServerEntityEvents.ENTITY_LOAD.register(mapPhase(phase), (entity, level) -> it.handle(level, entity)));

        LivingEntityCallback.Damage.EVENT.setup(FabricBalmSupplementalEvents.LIVING_DAMAGE::register);
        LivingEntityCallback.Fall.EVENT.setup(FabricBalmSupplementalEvents.LIVING_FALL::register);
        LivingEntityCallback.Heal.EVENT.setup(FabricBalmSupplementalEvents.LIVING_HEAL::register);
        LivingEntityCallback.Death.PRE.setup((phase, it)
                -> ServerLivingEntityEvents.ALLOW_DEATH.register((livingEntity, damageSource, damage) -> it.handle(livingEntity, damageSource).shouldSkipDefault()));
        LivingEntityCallback.Death.POST.setup((phase, it)
                -> ServerLivingEntityEvents.AFTER_DEATH.register(it::handle));

        PlayerCallback.Attack.EVENT.setup(FabricBalmSupplementalEvents.PLAYER_ATTACK::register);

        CropCallback.Grow.PRE.setup(FabricBalmSupplementalEvents.CROP_GROW_PRE::register);
        CropCallback.Grow.POST.setup(FabricBalmSupplementalEvents.CROP_GROW_POST::register);

        BlockCallback.Use.EVENT.setup((phase, it)
                -> UseBlockCallback.EVENT.register(mapPhase(phase), it::handle));
        BlockCallback.DigSpeed.EVENT.setup(BalmSupplementalEvents.BLOCK_DIG_SPEED::register);
        BlockCallback.Break.EVENT.setup((phase, it)
                -> PlayerBlockBreakEvents.BEFORE.register(mapPhase(phase), (world, player, pos, state, blockEntity) -> !it.handle(world, pos, state, blockEntity, player).shouldSkipDefault()));

        CreativeModeTabCallback.BUILD_CONTENTS.setup((phase, it)
                -> ItemGroupEvents.MODIFY_ENTRIES_ALL.register(mapPhase(phase), it::handle));
    }

    public static ResourceLocation mapPhase(ResourceLocation phase) {
        return PRIORITIES.getOrDefault(phase, Event.DEFAULT_PHASE);
    }
}
