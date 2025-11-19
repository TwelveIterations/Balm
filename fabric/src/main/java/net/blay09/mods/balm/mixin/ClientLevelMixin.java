package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LevelLoadingEvent;
import net.blay09.mods.balm.fabric.client.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
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
    private void onConstructor(ClientPacketListener clientPacketListener, ClientLevel.ClientLevelData clientLevelData, ResourceKey<Level> resourceKey, Holder<DimensionType> holder, int i, int j, LevelRenderer levelRenderer, boolean bl, long l, int i2, CallbackInfo ci) {
        ClientLevel clientLevel = (ClientLevel) (Object) this;
        Balm.events().fireEvent(new LevelLoadingEvent.Load(clientLevel));
        FabricBalmSupplementalClientEvents.CLIENT_LEVEL_LOAD.invoker().handle(clientLevel);
    }
}
