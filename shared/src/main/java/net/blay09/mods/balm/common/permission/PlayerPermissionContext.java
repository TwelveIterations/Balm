package net.blay09.mods.balm.common.permission;

import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public record PlayerPermissionContext(ServerPlayer player) implements PermissionContext {

    @Override
    public Optional<ServerPlayer> getPlayer() {
        return Optional.of(player);
    }

    @Override
    public Optional<UUID> getPlayerUUID() {
        return Optional.of(player.getUUID());
    }

    @Override
    public Optional<CommandSourceStack> getCommandSource() {
        return Optional.of(player.createCommandSourceStack());
    }
}
