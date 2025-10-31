package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.loot.UnpackedLootTableHolder;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RandomizableContainer.class)
public interface RandomizableContainerMixin {

    @Inject(method = "unpackLootTable(Lnet/minecraft/world/entity/player/Player;)V", at = @At("HEAD"))
    private void unpackLootTable(Player player, CallbackInfo ci) {
        final var lootTable = ((RandomizableContainer) this).getLootTable();
        if (this instanceof UnpackedLootTableHolder unpackedLootTableHolder && lootTable != null) {
            unpackedLootTableHolder.balm$setUnpackedLootTable(lootTable);
        }
    }

}
