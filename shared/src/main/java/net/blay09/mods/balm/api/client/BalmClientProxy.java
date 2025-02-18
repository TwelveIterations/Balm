package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BalmClientProxy extends BalmProxy {
    @Override
    public @Nullable Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isConnectedToServer() {
        return Minecraft.getInstance().getConnection() != null;
    }

    @Override
    public boolean isIngame() {
        return Minecraft.getInstance().gameMode != null;
    }

    @Override
    public boolean isClient() {
        return true;
    }
}
