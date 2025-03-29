package net.blay09.mods.balm.api;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BalmProxy {
    @Nullable
    public Player getClientPlayer() {
        return null;
    }

    public boolean isLocalServer() {
        return false;
    }

    public boolean isConnected() {
        return false;
    }

    public boolean isIngame() {
        return false;
    }

    public boolean isClient() {
        return false;
    }

    /**
     * @deprecated Use {@link #isConnected()} instead.
     */
    @Deprecated
    public final boolean isConnectedToServer() {
        return isConnected();
    }
}
