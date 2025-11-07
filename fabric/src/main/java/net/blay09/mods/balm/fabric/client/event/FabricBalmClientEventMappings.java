package net.blay09.mods.balm.fabric.client.event;

import net.blay09.mods.balm.client.event.callback.ClientLevelTickCallback;
import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.blay09.mods.balm.fabric.event.FabricBalmEventMappings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FabricBalmClientEventMappings extends FabricBalmEventMappings {

    public static void bind() {
        ClientTickCallback.PRE.setup((phase, it)
                -> ClientTickEvents.START_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.POST.setup((phase, it)
                -> ClientTickEvents.END_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientLevelTickCallback.PRE.setup((phase, it)
                -> ClientTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ClientLevelTickCallback.POST.setup((phase, it)
                -> ClientTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));

        // TODO ServerPlayerTickCallback.PRE.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
        // TODO ServerPlayerTickCallback.POST.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
        // TODO ServerEntityTickCallback.PRE.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
        // TODO ServerEntityTickCallback.POST.bind((phase, it) -> ServerTickEvents.register(mapPhase(phase), it::handle));
    }

}
