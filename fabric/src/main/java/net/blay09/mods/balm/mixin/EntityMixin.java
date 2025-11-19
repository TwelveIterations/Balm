package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.nbt.BalmDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements BalmDataHolder {

    private CompoundTag balmData = new CompoundTag();
    private CompoundTag forgeBalmData = new CompoundTag();
    private CompoundTag neoForgeBalmData = new CompoundTag();

    @Inject(method = "load(Lnet/minecraft/world/level/storage/ValueInput;)V", at = @At("HEAD"))
    private void load(ValueInput input, CallbackInfo callbackInfo) {
        balmData = input.read("BalmData", CompoundTag.CODEC).orElseGet(() -> input.child("ForgeData")
                .flatMap(it -> it.child("PlayerPersisted"))
                .flatMap(it -> it.read("BalmData", CompoundTag.CODEC))
              .orElse(balmData));
        forgeBalmData = input.child("ForgeData")
                .flatMap(it -> it.child("PlayerPersisted"))
                .flatMap(it -> it.read("BalmData", CompoundTag.CODEC))
                .orElse(forgeBalmData);
        neoForgeBalmData = input.child("NeoForgeData")
                .flatMap(it -> it.child("PlayerPersisted"))
                .flatMap(it -> it.read("BalmData", CompoundTag.CODEC))
                .orElse(neoForgeBalmData);
    }

    @Inject(method = "saveWithoutId(Lnet/minecraft/world/level/storage/ValueOutput;)V", at = @At("HEAD"))
    private void saveWithoutId(ValueOutput output, CallbackInfo callbackInfo) {
        if (!balmData.isEmpty()) {
            output.store("BalmData", CompoundTag.CODEC,balmData);
        }
    }

    @Override
    public CompoundTag getFabricBalmData() {
        return balmData;
    }

    @Override
    public void setFabricBalmData(CompoundTag tag) {
        this.balmData = tag;
    }

    @Override
    public CompoundTag getForgeBalmData() {
        return forgeBalmData;
    }

    @Override
    public void setForgeBalmData(CompoundTag tag) {
        this.forgeBalmData = tag;
    }

    @Override
    public CompoundTag getNeoForgeBalmData() {
        return neoForgeBalmData;
    }

    @Override
    public void setNeoForgeBalmData(CompoundTag tag) {
        this.neoForgeBalmData = tag;
    }
}
