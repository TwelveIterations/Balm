package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BalmClientProxy extends BalmProxy {
    @Override
    public @Nullable Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public boolean isLocalServer() {
        final var client = Minecraft.getInstance();
        return client != null && client.isLocalServer();
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public boolean isConnected() {
        final var client = Minecraft.getInstance();
        return client != null && client.getConnection() != null;
    }

    @Override
    @SuppressWarnings("ConstantValue")
    public boolean isIngame() {
        final var client = Minecraft.getInstance();
        return client != null && client.gameMode != null;
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public @Nullable Connection getConnection() {
        final var packetListener = Minecraft.getInstance().getConnection();
        return packetListener != null ? packetListener.getConnection() : null;
    }

    @Override
    public @Nullable ClientGamePacketListener getPacketListener() {
        return Minecraft.getInstance().getConnection();
    }
}
