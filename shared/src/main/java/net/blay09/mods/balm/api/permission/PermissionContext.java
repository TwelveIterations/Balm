package net.blay09.mods.balm.api.permission;

import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public interface PermissionContext {
    Optional<ServerPlayer> getPlayer();
    UUID getPlayerUUID();
}
