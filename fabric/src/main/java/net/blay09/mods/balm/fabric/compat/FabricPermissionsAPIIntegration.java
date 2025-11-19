package net.blay09.mods.balm.fabric.compat;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.blay09.mods.balm.common.permission.CommonBalmPermissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class FabricPermissionsAPIIntegration extends CommonBalmPermissions {
    private static String toPermission(Identifier permission) {
        return permission.getNamespace() + "." + permission.getPath();
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Identifier permission) {
        return Permissions.check(player, toPermission(permission), super.hasPermission(player, permission));
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Identifier permission) {
        return Permissions.check(source, toPermission(permission), super.hasPermission(source, permission));
    }
}
