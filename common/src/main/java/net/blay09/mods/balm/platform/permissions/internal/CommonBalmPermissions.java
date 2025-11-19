package net.blay09.mods.balm.platform.permissions.internal;

import net.blay09.mods.balm.platform.permissions.BalmPermissions;
import net.blay09.mods.balm.platform.permissions.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommonBalmPermissions implements BalmPermissions {

    private final Map<Identifier, Function<PermissionContext, Boolean>> resolvers = new HashMap<>();

    @Override
    public void registerPermission(Identifier permission, Function<PermissionContext, Boolean> defaultResolver) {
        resolvers.put(permission, defaultResolver);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Identifier permission) {
        final var node = resolvers.get(permission);
        if (node == null) {
            return false;
        }

        return node.apply(new PlayerPermissionContext(player));
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Identifier permission) {
        final var node = resolvers.get(permission);
        if (node == null) {
            return false;
        }

        return node.apply(new CommandPermissionContext(source));
    }
}
