package net.blay09.mods.balm.client.platform.internal;

import net.blay09.mods.balm.platform.BalmSafeClientAccess;
import net.blay09.mods.kuma.api.Kuma;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeMap;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class BalmClientSafeClientAccess extends BalmSafeClientAccess {

    @Nullable
    private RecipeMap syncedRecipes;

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

    @Override
    public Optional<RecipeMap> getRecipeMap() {
        final var serverRecipeMap = super.getRecipeMap();
        if (serverRecipeMap.isPresent()) {
            return serverRecipeMap;
        }

        return Optional.ofNullable(syncedRecipes);
    }

    public void setSyncedRecipes(@Nullable RecipeMap syncedRecipes) {
        this.syncedRecipes = syncedRecipes;
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
