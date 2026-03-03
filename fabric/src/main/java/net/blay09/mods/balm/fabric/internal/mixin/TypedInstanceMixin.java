package net.blay09.mods.balm.fabric.internal.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TypedInstance.class)
public interface TypedInstanceMixin {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "is(Lnet/minecraft/core/Holder;)Z", at = @At("RETURN"), cancellable = true)
    default void is(Holder holder, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && ((TypedInstance) this).is(holder.value())) {
            cir.setReturnValue(true);
        }
    }
}
