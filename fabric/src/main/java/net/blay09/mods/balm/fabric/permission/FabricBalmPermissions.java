package net.blay09.mods.balm.fabric.permission;

import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.permission.PermissionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public class FabricBalmPermissions implements BalmPermissions {
    @Override
    public void registerPermission(ResourceLocation permission, Function<PermissionContext, Boolean> defaultResolver) {
        // TODO
    }

    @Override
    public boolean hasPermission(ServerPlayer player, ResourceLocation permission) {
        return false; // TODO
    }
}
