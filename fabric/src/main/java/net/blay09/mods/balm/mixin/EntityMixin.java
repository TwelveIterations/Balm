package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.entity.BalmEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements BalmEntity {

    private CompoundTag balmData = new CompoundTag();
    private CompoundTag forgeBalmData = new CompoundTag();
    private CompoundTag neoforgeBalmData = new CompoundTag();

    @Inject(method = "load(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    private void load(CompoundTag compound, CallbackInfo callbackInfo) {
        if (compound.contains("BalmData")) {
            balmData = compound.getCompound("BalmData").orElseGet(() -> compound.getCompound("ForgeData")
                    .flatMap(it -> it.getCompound("PlayerPersisted"))
                    .flatMap(it -> it.getCompound("BalmData"))
                    .orElse(new CompoundTag()));
        }
        if (compound.contains("ForgeData")) {
            forgeBalmData = compound.getCompound("ForgeData")
                    .flatMap(it -> it.getCompound("PlayerPersisted"))
                    .flatMap(it -> it.getCompound("BalmData"))
                    .orElse(new CompoundTag());
        }
        if (compound.contains("NeoForgeData")) {
            neoforgeBalmData = compound.getCompound("NeoForgeData")
                    .flatMap(it -> it.getCompound("PlayerPersisted"))
                    .flatMap(it -> it.getCompound("BalmData"))
                    .orElse(new CompoundTag());
        }
    }

    @Inject(method = "saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("HEAD"))
    private void saveWithoutId(CompoundTag compound, CallbackInfoReturnable<CompoundTag> callbackInfo) {
        if (!balmData.isEmpty()) {
            compound.put("BalmData", balmData);
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
        return neoforgeBalmData;
    }

    @Override
    public void setNeoForgeBalmData(CompoundTag tag) {
        this.neoforgeBalmData = tag;
    }
}
