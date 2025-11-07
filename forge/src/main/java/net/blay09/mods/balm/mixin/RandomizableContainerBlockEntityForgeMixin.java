package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.world.level.storage.loot.UnpackedLootTableHolder;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Forge's mixin does not support injecting into the mixin, so we can't inject into the default method at all.
 * This mixin works around that by injecting into every single method that calls unpackLootTables. Thanks Forge!
 */
@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityForgeMixin {

    @Inject(method = "isEmpty", at = @At(("HEAD")))
    public void isEmpty(CallbackInfoReturnable<Boolean> cir) {
        extractLootTable();
    }

    @Inject(method = "getItem", at = @At(("HEAD")))
    public void getItem(int slot, CallbackInfoReturnable<ItemStack> cir) {
        extractLootTable();
    }

    @Inject(method = "removeItem", at = @At(("HEAD")))
    public void removeItem(int slot, int count, CallbackInfoReturnable<ItemStack> cir) {
        extractLootTable();
    }

    @Inject(method = "removeItemNoUpdate", at = @At(("HEAD")))
    public void removeItemNoUpdate(int slot, CallbackInfoReturnable<ItemStack> cir) {
        extractLootTable();
    }

    @Inject(method = "setItem", at = @At(("HEAD")))
    public void setItem(int slot, ItemStack itemStack, CallbackInfo ci) {
        extractLootTable();
    }

    private void extractLootTable() {
        final var lootTable = ((RandomizableContainer) this).getLootTable();
        if (this instanceof UnpackedLootTableHolder unpackedLootTableHolder && lootTable != null) {
            unpackedLootTableHolder.balm$setUnpackedLootTable(lootTable);
        }
    }
}
