package net.blay09.mods.balm.internal.mixin;

import net.blay09.mods.balm.client.platform.event.internal.BalmSupplementalClientEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "extractBlockOutline(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/state/level/LevelRenderState;)V", at = @At("RETURN"), cancellable = true)
    public void extractBlockOutline(Camera camera, LevelRenderState levelRenderState, CallbackInfo ci) {
        if (minecraft.hitResult instanceof BlockHitResult blockHitResult && levelRenderState.blockOutlineRenderState != null) {
            if (!BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT.invoker()
                    .shouldRender(blockHitResult, camera, levelRenderState)) {
                levelRenderState.blockOutlineRenderState = null;
            }
        }
    }

}
