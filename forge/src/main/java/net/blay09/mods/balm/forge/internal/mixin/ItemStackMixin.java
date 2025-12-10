package net.blay09.mods.balm.forge.internal.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "is(Lnet/minecraft/core/Holder;)Z", at = @At("RETURN"), cancellable = true)
    public void is(Holder<Item> holder, CallbackInfoReturnable<Boolean> cir) {
        //noinspection ConstantValue
        cir.setReturnValue(cir.getReturnValue() || ((ItemStack) (Object) this).is(holder.value()));
    }
}
