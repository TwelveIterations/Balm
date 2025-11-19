package net.blay09.mods.balm.neoforge.internal.mixin;

import net.blay09.mods.balm.nbt.BalmDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements BalmDataHolder {

    @Unique
    private CompoundTag balm$fabricBalmData = new CompoundTag();
    @Unique
    private CompoundTag balm$forgeBalmData = new CompoundTag();

    @Inject(method = "load(Lnet/minecraft/world/level/storage/ValueInput;)V", at = @At("HEAD"))
    private void load(ValueInput input, CallbackInfo callbackInfo) {
        balm$fabricBalmData = input.read("BalmData", CompoundTag.CODEC).orElse(balm$fabricBalmData);
        balm$forgeBalmData = input.child("ForgeData")
                .flatMap(it -> it.child("PlayerPersisted"))
                .flatMap(it -> it.read("BalmData", CompoundTag.CODEC))
                .orElse(balm$forgeBalmData);
    }

    @Override
    public CompoundTag balm$getFabricBalmData() {
        return balm$fabricBalmData;
    }

    @Override
    public void balm$setFabricBalmData(CompoundTag tag) {
        this.balm$fabricBalmData = tag;
    }

    @Override
    public CompoundTag balm$getForgeBalmData() {
        return balm$forgeBalmData;
    }

    @Override
    public void balm$setForgeBalmData(CompoundTag tag) {
        this.balm$forgeBalmData = tag;
    }

    @Override
    public CompoundTag balm$getNeoForgeBalmData() {
        throw new UnsupportedOperationException("This method should not have been called. Report this issue to Balm.");
    }

    @Override
    public void balm$setNeoForgeBalmData(CompoundTag tag) {
        throw new UnsupportedOperationException("This method should not have been called. Report this issue to Balm.");
    }
}
