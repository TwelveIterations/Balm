package net.blay09.mods.balm.common.permission;

import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CommonBalmPermissions implements BalmPermissions {

    private final Map<ResourceLocation, Function<PermissionContext, Boolean>> resolvers = new ConcurrentHashMap<>();

    @Override
    public void registerPermission(ResourceLocation permission, Function<PermissionContext, Boolean> defaultResolver) {
        resolvers.put(permission, defaultResolver);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        final var node = resolvers.get(permission);
        if (node == null) {
            return false;
        }

        return node.apply(new PlayerPermissionContext(player));
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, ResourceLocation permission) {
        final var node = resolvers.get(permission);
        if (node == null) {
            return false;
        }

        return node.apply(new CommandPermissionContext(source));
    }
}
