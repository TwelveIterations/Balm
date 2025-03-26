package net.blay09.mods.balm.common.permission;

import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public record CommandPermissionContext(CommandSourceStack source) implements PermissionContext {

    @Override
    public Optional<ServerPlayer> getPlayer() {
        return Optional.ofNullable(source.getPlayer());
    }

    @Override
    public Optional<UUID> getPlayerUUID() {
        return Optional.ofNullable(source.getPlayer()).map(ServerPlayer::getUUID);
    }

    @Override
    public Optional<CommandSourceStack> getCommandSource() {
        return Optional.of(source);
    }
}
