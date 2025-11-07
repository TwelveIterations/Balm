package net.blay09.mods.balm.fabric.event;

import net.blay09.mods.balm.event.EventPhases;
import net.blay09.mods.balm.event.callback.ServerLevelTickCallback;
import net.blay09.mods.balm.event.callback.ServerTickCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class FabricBalmEventMappings {
    private static final Map<ResourceLocation, ResourceLocation> PRIORITIES = Map.of(
            EventPhases.LOWEST, EventPhases.LOWEST,
            EventPhases.LOW, EventPhases.LOW,
            EventPhases.DEFAULT, Event.DEFAULT_PHASE,
            EventPhases.HIGH, EventPhases.HIGH,
            EventPhases.HIGHEST, EventPhases.HIGHEST
    );

    public static void bind() {
        ServerTickCallback.PRE.setup((phase, it)
                -> ServerTickEvents.START_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerTickCallback.POST.setup((phase, it)
                -> ServerTickEvents.END_SERVER_TICK.register(mapPhase(phase), it::handle));
        ServerLevelTickCallback.PRE.setup((phase, it)
                -> ServerTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ServerLevelTickCallback.POST.setup((phase, it)
                -> ServerTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));

        // TODO ServerPlayerTickCallback.PRE.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
        // TODO ServerPlayerTickCallback.POST.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
        // TODO ServerEntityTickCallback.PRE.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
        // TODO ServerEntityTickCallback.POST.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
    }

    public static ResourceLocation mapPhase(ResourceLocation phase) {
        return PRIORITIES.getOrDefault(phase, Event.DEFAULT_PHASE);
    }
}
