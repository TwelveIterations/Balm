package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.blay09.mods.balm.fabric.block.entity.BlockEntityOnLoadCallback;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LevelChunk.class)
public class LevelChunkMixin {

    @Shadow
    private boolean loaded;

    @Shadow
    private Level level;

    @Inject(method = "registerAllBlockEntitiesAfterLevelLoad", at = @At("HEAD"))
    private void registerAllBlockEntitiesAfterLevelLoad(CallbackInfo callbackInfo) {
        BlockEntityOnLoadCallback.scheduleOnLoad(((LevelChunk) (Object) this).getBlockEntities().values());
    }

    @Inject(method = "addAndRegisterBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At("RETURN"))
    private void addAndRegisterBlockEntity(BlockEntity blockEntity, CallbackInfo callbackInfo) {
        if ((this.loaded || this.level.isClientSide()) && blockEntity instanceof OnLoadHandler handler) {
            BlockEntityOnLoadCallback.scheduleOnLoad(List.of(blockEntity));
        }
    }

}
