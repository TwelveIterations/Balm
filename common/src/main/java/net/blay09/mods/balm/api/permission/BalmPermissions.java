package net.blay09.mods.balm.api.permission;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public interface BalmPermissions {
    void registerPermission(Identifier permission, Function<PermissionContext, Boolean> defaultResolver);

    boolean hasPermission(ServerPlayer player, Identifier permission);

    boolean hasPermission(CommandSourceStack source, Identifier permission);
}
