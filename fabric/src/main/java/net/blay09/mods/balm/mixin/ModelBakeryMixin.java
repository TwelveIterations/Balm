package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    @Inject(method = "bakeModels(Ljava/util/function/BiFunction;)V", at = @At("RETURN"))
    private void apply(BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction, CallbackInfo callbackInfo) {
        FabricBalmModels.onBakeModels((ModelBakery) (Object) this, spriteBiFunction);
    }
}
