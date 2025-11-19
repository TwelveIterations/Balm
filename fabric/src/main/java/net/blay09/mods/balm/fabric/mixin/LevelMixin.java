package net.blay09.mods.balm.fabric.mixin;

import net.blay09.mods.balm.fabric.world.level.block.entity.internal.BlockEntityOnLoadCallback;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(method = "tickBlockEntities", at = @At("HEAD"))
    private void tickBlockEntities(CallbackInfo callbackInfo) {
        BlockEntityOnLoadCallback.fireOnLoad((Level) (Object) this);
    }
}
