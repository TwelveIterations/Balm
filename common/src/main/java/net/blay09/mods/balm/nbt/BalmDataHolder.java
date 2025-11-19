package net.blay09.mods.balm.nbt;

import net.minecraft.nbt.CompoundTag;

public interface BalmDataHolder {

    CompoundTag getFabricBalmData();

    void setFabricBalmData(CompoundTag tag);

    CompoundTag getForgeBalmData();

    void setForgeBalmData(CompoundTag tag);

    CompoundTag getNeoForgeBalmData();

    void setNeoForgeBalmData(CompoundTag tag);

}
