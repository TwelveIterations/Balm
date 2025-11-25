package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.RecipesUpdatedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Final
    @Shadow
    private RecipeManager recipeManager;

    @SuppressWarnings("removal")
    @Inject(method = "handleUpdateRecipes(Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;)V", at = @At("RETURN"))
    void handleUpdateRecipes(ClientboundUpdateRecipesPacket packet, CallbackInfo callbackInfo) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess(); // same way that Minecraft does it in the packet handler
        Balm.getEvents().fireEvent(new RecipesUpdatedEvent(recipeManager, registryAccess));
    }
}
