package net.blay09.mods.balm.platform.internal;

import net.blay09.mods.balm.platform.BalmSafeClientAccess;
import net.blay09.mods.kuma.api.Kuma;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BalmClientSafeClientAccess extends BalmSafeClientAccess {
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
    public boolean isShiftDown() {
        return Kuma.hasShiftDown();
    }

    @Override
    public boolean isControlDown() {
        return Kuma.hasControlDown();
    }

    @Override
    public boolean isAltDown() {
        return Kuma.hasAltDown();
    }
}
