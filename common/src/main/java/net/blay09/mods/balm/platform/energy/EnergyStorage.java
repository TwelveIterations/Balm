package net.blay09.mods.balm.platform.energy;

public interface EnergyStorage {

    int fill(int maxFill, boolean simulate);

    int drain(int maxDrain, boolean simulate);

    int getEnergy();

    void setEnergy(int energy);

    int getCapacity();

    boolean canDrain();

    boolean canFill();
}
