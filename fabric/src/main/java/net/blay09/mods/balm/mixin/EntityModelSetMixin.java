package net.blay09.mods.balm.mixin;

import com.google.common.collect.ImmutableMap;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmRenderers;
import net.blay09.mods.balm.fabric.client.rendering.FabricModelLayers;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityModelSet.class)
public class EntityModelSetMixin {

    @Inject(method = "onResourceManagerReload(Lnet/minecraft/server/packs/resources/ResourceManager;)V", at = @At("TAIL"))
    private void onResourceManagerReload(ResourceManager resourceManager, CallbackInfo callbackInfo) {
        Map<ModelLayerLocation, LayerDefinition> originalRoots = ((EntityModelSetAccessor) this).getRoots();
        ImmutableMap<ModelLayerLocation, LayerDefinition> roots = ImmutableMap.<ModelLayerLocation, LayerDefinition>builder()
                .putAll(originalRoots)
                .putAll(FabricModelLayers.createRoots())
                .build();
        ((EntityModelSetAccessor) this).setRoots(roots);
    }

}
