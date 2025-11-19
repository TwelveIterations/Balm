package net.blay09.mods.balm.nbt;

import net.minecraft.nbt.CompoundTag;

public interface BalmDataHolder {

    CompoundTag balm$getFabricBalmData();

    void balm$setFabricBalmData(CompoundTag tag);

    CompoundTag balm$getForgeBalmData();

    void balm$setForgeBalmData(CompoundTag tag);

    CompoundTag balm$getNeoForgeBalmData();

    void balm$setNeoForgeBalmData(CompoundTag tag);

}
