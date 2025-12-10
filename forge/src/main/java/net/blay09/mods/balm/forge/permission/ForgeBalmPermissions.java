package net.blay09.mods.balm.forge.permission;

import net.blay09.mods.balm.platform.permissions.PermissionContext;
import net.blay09.mods.balm.platform.permissions.internal.CommonBalmPermissions;
import net.blay09.mods.balm.platform.permissions.internal.OfflinePermissionContext;
import net.blay09.mods.balm.platform.permissions.internal.PlayerPermissionContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ForgeBalmPermissions extends CommonBalmPermissions {

    private final Map<Identifier, PermissionNode<?>> nodes = new HashMap<>();

    public ForgeBalmPermissions() {
        PermissionGatherEvent.Nodes.BUS.addListener(this::registerNodes);
    }

    private void registerNodes(PermissionGatherEvent.Nodes event) {
        event.addNodes(nodes.values());
    }

    @Override
    public void registerPermission(Identifier permission, Function<PermissionContext, Boolean> defaultResolver) {
        super.registerPermission(permission, defaultResolver);
        nodes.put(permission, new PermissionNode<>(permission, PermissionTypes.BOOLEAN,
                (serverPlayer, uuid, permissionDynamicContexts) ->
                        defaultResolver.apply(serverPlayer != null ? new PlayerPermissionContext(serverPlayer) : new OfflinePermissionContext(uuid))));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasPermission(ServerPlayer player, Identifier permission) {
        final var node = (PermissionNode<Boolean>) nodes.get(permission);
        if (node == null) {
            return false;
        }

        return PermissionAPI.getPermission(player, node);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasPermission(CommandSourceStack source, Identifier permission) {
        final var node = (PermissionNode<Boolean>) nodes.get(permission);
        if (node == null) {
            return false;
        }

        final var player = source.getPlayer();
        // Neo/Forge adds a super complex permission API but doesn't support command sources lol
        return player != null ? PermissionAPI.getPermission(player, node) : super.hasPermission(source, permission);
    }
}
