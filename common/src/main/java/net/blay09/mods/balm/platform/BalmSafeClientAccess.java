package net.blay09.mods.balm.platform;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.mixin.RecipeManagerAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeMap;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class BalmSafeClientAccess {
    @Nullable
    public Player getClientPlayer() {
        return null;
    }

    public boolean isLocalServer() {
        return false;
    }

    public boolean isConnected() {
        return false;
    }

    public boolean isIngame() {
        return false;
    }

    public boolean isClient() {
        return false;
    }

    public boolean isShiftDown() {
        return false;
    }

    public boolean isControlDown() {
        return false;
    }

    public boolean isAltDown() {
        return false;
    }

    public Optional<RecipeMap> getRecipeMap() {
        final var server = Balm.platform().server();
        if (server != null) {
            return Optional.of(((RecipeManagerAccessor) server.getRecipeManager()).balm$getRecipeMap());
        }

        return Optional.empty();
    }
}
