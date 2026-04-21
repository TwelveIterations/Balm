package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructor(ClientPacketListener connection, ClientLevel.ClientLevelData levelData, ResourceKey<Level> dimension, Holder<DimensionType> dimensionType, int serverChunkRadius, int serverSimulationDistance, LevelExtractor levelExtractor, boolean isDebug, long biomeZoomSeed, int seaLevel, CallbackInfo ci) {
        ClientLevel clientLevel = (ClientLevel) (Object) this;
        FabricBalmSupplementalClientEvents.CLIENT_LEVEL_LOAD.invoker().handle(clientLevel);
    }
}
