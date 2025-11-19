package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.event.client.ClientLevelTickHandler;
import net.blay09.mods.balm.api.event.client.ClientTickHandler;

@Deprecated
public class TickType<T> {

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ClientTickCallback} instead.
     */
    @Deprecated
    public static final TickType<ClientTickHandler> Client = new TickType<>();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ClientLevelTickCallback} instead.
     */
    @Deprecated
    public static final TickType<ClientLevelTickHandler> ClientLevel = new TickType<>();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerTickCallback} instead.
     */
    @Deprecated
    public static final TickType<ServerTickHandler> Server = new TickType<>();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerLevelTickCallback} instead.
     */
    @Deprecated
    public static final TickType<ServerLevelTickHandler> ServerLevel = new TickType<>();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerPlayerTickCallback} instead.
     */
    @Deprecated
    public static final TickType<ServerPlayerTickHandler> ServerPlayer = new TickType<>();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ClientEntityTickCallback} instead.
     */
    @Deprecated
    public static final TickType<EntityTickHandler> ClientEntity = new TickType<>();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerEntityTickCallback} instead.
     */
    @Deprecated
    public static final TickType<EntityTickHandler> ServerEntity = new TickType<>();
}
