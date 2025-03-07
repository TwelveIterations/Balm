package net.blay09.mods.balm.common.permission;

import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public record PlayerPermissionContext(ServerPlayer player) implements PermissionContext {

    @Override
    public Optional<ServerPlayer> getPlayer() {
        return Optional.of(player);
    }

    @Override
    public UUID getPlayerUUID() {
        return player.getUUID();
    }
}
