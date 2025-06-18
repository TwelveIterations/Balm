package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.entity.BalmEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements BalmEntity {

    private CompoundTag fabricBalmData = new CompoundTag();
    private CompoundTag neoforgeBalmData = new CompoundTag();

    @Inject(method = "load(Lnet/minecraft/world/level/storage/ValueInput;)V", at = @At("HEAD"))
    private void load(ValueInput input, CallbackInfo callbackInfo) {
        fabricBalmData = input.read("BalmData", CompoundTag.CODEC).orElse(fabricBalmData);
        neoforgeBalmData = input.child("NeoForgeData")
                .flatMap(it -> it.child("PlayerPersisted"))
                .flatMap(it -> it.read("BalmData", CompoundTag.CODEC))
                .orElse(neoforgeBalmData);
    }

    @Override
    public CompoundTag getFabricBalmData() {
        return fabricBalmData;
    }

    @Override
    public void setFabricBalmData(CompoundTag tag) {
        this.fabricBalmData = tag;
    }

    @Override
    public CompoundTag getForgeBalmData() {
        throw new UnsupportedOperationException("This method should not have been called. Report this issue to Balm.");
    }

    @Override
    public void setForgeBalmData(CompoundTag tag) {
        throw new UnsupportedOperationException("This method should not have been called. Report this issue to Balm.");
    }

    @Override
    public CompoundTag getNeoForgeBalmData() {
        return neoforgeBalmData;
    }

    @Override
    public void setNeoForgeBalmData(CompoundTag tag) {
        this.neoforgeBalmData = tag;
    }
}
