package net.blay09.mods.balm.forge.client.event;

import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.blay09.mods.balm.forge.event.ForgeBalmEventMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.LogicalSide;

public class ForgeBalmClientEventMappings extends ForgeBalmEventMappings {
    public static void bind() {
        bindSimple(ClientTickCallback.PRE, TickEvent.ClientTickEvent.Pre.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientTickCallback.POST, TickEvent.ClientTickEvent.Post.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindFiltered(ClientTickCallback.Level.PRE, TickEvent.LevelTickEvent.Pre.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((ClientLevel) event.level()));
        bindFiltered(ClientTickCallback.Level.POST, TickEvent.LevelTickEvent.Post.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((ClientLevel) event.level()));
        bindFiltered(ClientTickCallback.Player.PRE, TickEvent.PlayerTickEvent.Pre.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((AbstractClientPlayer) event.player()));
        bindFiltered(ClientTickCallback.Player.POST, TickEvent.PlayerTickEvent.Post.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((AbstractClientPlayer) event.player()));
        bindSimple(ClientTickCallback.Entity.PRE, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));
        bindSimple(ClientTickCallback.Entity.POST, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));
    }
}
