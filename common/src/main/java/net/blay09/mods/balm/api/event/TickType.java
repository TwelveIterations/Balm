package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.event.client.ClientLevelTickHandler;
import net.blay09.mods.balm.api.event.client.ClientTickHandler;

public class TickType<T> {
    public static final TickType<ClientTickHandler> Client = new TickType<>();
    public static final TickType<ClientLevelTickHandler> ClientLevel = new TickType<>();
    public static final TickType<ServerTickHandler> Server = new TickType<>();
    public static final TickType<ServerLevelTickHandler> ServerLevel = new TickType<>();
    public static final TickType<ServerPlayerTickHandler> ServerPlayer = new TickType<>();
}
