package net.blay09.mods.balm.internal.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blay09.mods.balm.client.platform.event.BalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
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
    private RenderBuffers renderBuffers;

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDLnet/minecraft/client/renderer/state/BlockOutlineRenderState;IF)V", at = @At("HEAD"), cancellable = true)
    public void renderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, double x, double y, double z, BlockOutlineRenderState state, int color, float f, CallbackInfo callbackInfo) {
        if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
            if (!BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT.invoker()
                    .shouldRender(blockHitResult, poseStack, renderBuffers.bufferSource(), minecraft.gameRenderer.getMainCamera())) {
                callbackInfo.cancel();
            }
        }
    }

}
