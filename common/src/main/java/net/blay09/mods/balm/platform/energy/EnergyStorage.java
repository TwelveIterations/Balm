package net.blay09.mods.balm.platform.energy;

public interface EnergyStorage {

    long fill(long maxFill, boolean simulate);

    long drain(long maxDrain, boolean simulate);

    long getEnergy();

    void setEnergy(long energy);

    long getCapacity();

    boolean canDrain();

    boolean canFill();
}
