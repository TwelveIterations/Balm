package net.blay09.mods.balm.client.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface ClientTickCallback {
    void handle(Minecraft client);

    EventMapper<ClientTickCallback> BEFORE = EventMapper.createUnbound("ClientTickCallback.Before");
    EventMapper<ClientTickCallback> AFTER = EventMapper.createUnbound("ClientTickCallback.After");

    @FunctionalInterface
    interface ClientPlayerTick {
        void handle(AbstractClientPlayer player);

        EventMapper<ClientPlayerTick> BEFORE = EventMapper.createUnbound("ClientPlayerTick.Before");
        EventMapper<ClientPlayerTick> AFTER = EventMapper.createUnbound("ClientPlayerTick.After");
    }

    @FunctionalInterface
    interface ClientLevelTick {
        void handle(ClientLevel level);

        EventMapper<ClientLevelTick> BEFORE = EventMapper.createUnbound("ClientLevelTick.Before");
        EventMapper<ClientLevelTick> AFTER = EventMapper.createUnbound("ClientLevelTick.After");
    }

    @FunctionalInterface
    interface ClientEntityTick {
        void handle(Entity entity);

        EventMapper<ClientEntityTick> BEFORE = EventMapper.createUnbound("ClientEntityTick.Before");
        EventMapper<ClientEntityTick> AFTER = EventMapper.createUnbound("ClientEntityTick.After");
    }

}
