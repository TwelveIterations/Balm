package net.blay09.mods.balm.fabric.permission;

import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.blay09.mods.balm.common.permission.PlayerPermissionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultFabricBalmPermissions implements BalmPermissions {

    private static final Map<ResourceLocation, Function<PermissionContext, Boolean>> nodes = new HashMap<>();

    @Override
    public void registerPermission(ResourceLocation permission, Function<PermissionContext, Boolean> defaultResolver) {
        nodes.put(permission, defaultResolver);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        final var node = nodes.get(permission);
        if (node == null) {
            return false;
        }

        return node.apply(new PlayerPermissionContext(player));
    }
}
