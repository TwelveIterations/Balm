package net.blay09.mods.balm.fabric.compat;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.blay09.mods.balm.fabric.permission.DefaultFabricBalmPermissions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class FabricPermissionsAPIIntegration extends DefaultFabricBalmPermissions {
    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        return Permissions.check(player, permission.getNamespace() + "." + permission.getPath(), super.hasPermission(player, permission));
    }
}
