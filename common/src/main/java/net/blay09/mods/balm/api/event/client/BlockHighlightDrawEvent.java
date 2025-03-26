package net.blay09.mods.balm.api.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;

public class BlockHighlightDrawEvent extends BalmEvent {
    private final BlockHitResult hitResult;
    private final PoseStack poseStack;
    private final MultiBufferSource multiBufferSource;
    private final Camera camera;

    public BlockHighlightDrawEvent(BlockHitResult hitResult, PoseStack poseStack, MultiBufferSource multiBufferSource, Camera camera) {
        this.hitResult = hitResult;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.camera = camera;
    }

    public BlockHitResult getHitResult() {
        return hitResult;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }

    public Camera getCamera() {
        return camera;
    }
}
