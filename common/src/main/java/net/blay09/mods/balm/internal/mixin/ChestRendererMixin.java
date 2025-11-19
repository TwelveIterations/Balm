package net.blay09.mods.balm.internal.mixin;

import net.blay09.mods.balm.client.renderer.CustomChestMaterials;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChestRenderer.class)
public class ChestRendererMixin {
    @ModifyVariable(method = "submit*", at = @At("STORE"), ordinal = 0)
    private static Material chooseMaterial(Material material, ChestRenderState renderState) {
        final var newMaterial = CustomChestMaterials.getMaterial(renderState);
        return newMaterial != null ? newMaterial : material;
    }
}
