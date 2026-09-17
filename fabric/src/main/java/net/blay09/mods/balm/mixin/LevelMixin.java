package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.blay09.mods.balm.fabric.block.entity.BlockEntityOnLoadCallback;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(Level.class)
public class LevelMixin implements BlockEntityOnLoadCallback {

    @Unique
    private List<BlockEntity> balm$pendingBlockEntityOnLoadCallbacks = new ArrayList<>();

    @Override
    public void balm$scheduleBlockEntityOnLoad(Collection<BlockEntity> blockEntities) {
        balm$pendingBlockEntityOnLoadCallbacks.addAll(blockEntities);
    }

    @Inject(method = "tickBlockEntities", at = @At("HEAD"))
    private void tickBlockEntities(CallbackInfo callbackInfo) {
        final var blockEntities = balm$pendingBlockEntityOnLoadCallbacks;
        balm$pendingBlockEntityOnLoadCallbacks = new ArrayList<>();

        for (final var blockEntity : blockEntities) {
            if (blockEntity instanceof OnLoadHandler handler) {
                handler.onLoad();
            }
        }
    }
}
