package net.blay09.mods.balm.platform.permissions.internal;

import net.blay09.mods.balm.platform.permissions.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public record OfflinePermissionContext(UUID playerUUID) implements PermissionContext {

    @Override
    public Optional<ServerPlayer> getPlayer() {
        return Optional.empty();
    }

    @Override
    public Optional<UUID> getPlayerUUID() {
        return Optional.of(playerUUID);
    }

    @Override
    public Optional<CommandSourceStack> getCommandSource() {
        return Optional.empty();
    }
}
