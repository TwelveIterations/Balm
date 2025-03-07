package net.blay09.mods.balm.forge.permission;

import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.blay09.mods.balm.common.permission.OfflinePermissionContext;
import net.blay09.mods.balm.common.permission.PlayerPermissionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ForgeBalmPermissions implements BalmPermissions {

    private final Map<ResourceLocation, PermissionNode<?>> nodes = new HashMap<>();

    public ForgeBalmPermissions() {
        MinecraftForge.EVENT_BUS.addListener(this::registerNodes);
    }


    private void registerNodes(PermissionGatherEvent.Nodes event) {
        event.addNodes(nodes.values());
    }

    @Override
    public void registerPermission(ResourceLocation permission, Function<PermissionContext, Boolean> defaultResolver) {
        nodes.put(permission, new PermissionNode<>(permission, PermissionTypes.BOOLEAN,
                (serverPlayer, uuid, permissionDynamicContexts) ->
                        defaultResolver.apply(serverPlayer != null ? new PlayerPermissionContext(serverPlayer) : new OfflinePermissionContext(uuid))));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        final var node = (PermissionNode<Boolean>) nodes.get(permission);
        if (node == null) {
            return false;
        }
        return PermissionAPI.getPermission(player, node);
    }
}
