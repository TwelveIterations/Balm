package net.blay09.mods.balm.common.permission;

import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public record OfflinePermissionContext(UUID playerUUID) implements PermissionContext {

    @Override
    public Optional<ServerPlayer> getPlayer() {
        return Optional.empty();
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
