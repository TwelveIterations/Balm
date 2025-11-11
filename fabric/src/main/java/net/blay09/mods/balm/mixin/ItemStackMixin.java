package net.blay09.mods.balm.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "is(Lnet/minecraft/core/Holder;)Z", at = @At("HEAD"), cancellable = true)
    private void onCrafted(Holder<Item> holder, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(((ItemStack) (Object) this).is(holder.value()));
    }
}
