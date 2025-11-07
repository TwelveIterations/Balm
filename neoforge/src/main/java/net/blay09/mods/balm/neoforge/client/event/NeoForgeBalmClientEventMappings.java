package net.blay09.mods.balm.neoforge.client.event;

import net.blay09.mods.balm.client.event.callback.ClientEntityTickCallback;
import net.blay09.mods.balm.client.event.callback.ClientLevelTickCallback;
import net.blay09.mods.balm.client.event.callback.ClientPlayerTickCallback;
import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEventMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class NeoForgeBalmClientEventMappings extends NeoForgeBalmEventMappings {

    public static void bind() {
        bindSimple(ClientTickCallback.PRE, ClientTickEvent.Pre.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientTickCallback.POST, ClientTickEvent.Post.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindFiltered(ClientLevelTickCallback.PRE, LevelTickEvent.Pre.class, event -> event.getLevel().isClientSide(), (event, it) -> it.handle((ClientLevel) event.getLevel()));
        bindFiltered(ClientLevelTickCallback.POST, LevelTickEvent.Post.class, event -> event.getLevel().isClientSide(), (event, it) -> it.handle((ClientLevel) event.getLevel()));
        bindFiltered(ClientPlayerTickCallback.PRE, PlayerTickEvent.Pre.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle((AbstractClientPlayer) event.getEntity()));
        bindFiltered(ClientPlayerTickCallback.POST, PlayerTickEvent.Post.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle((AbstractClientPlayer) event.getEntity()));
        bindFiltered(ClientEntityTickCallback.PRE, EntityTickEvent.Pre.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));
        bindFiltered(ClientEntityTickCallback.POST, EntityTickEvent.Post.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));
    }

}
