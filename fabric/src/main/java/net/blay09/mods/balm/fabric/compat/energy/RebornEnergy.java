package net.blay09.mods.balm.fabric.compat.energy;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.platform.capabilities.CommonCapabilities;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class RebornEnergy {
    public RebornEnergy() {
        EnergyStorage.SIDED.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            private boolean running;

            @Override
            public @Nullable EnergyStorage find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
                if (running) {
                    return null;
                }

                if (blockEntity instanceof BalmEnergyStorageProvider energyStorageProvider) {
                    final var energyStorage = direction != null ? energyStorageProvider.getEnergyStorage(direction) : energyStorageProvider.getEnergyStorage();
                    if (energyStorage != null) {
                        return new RebornEnergyStorage(energyStorage);
                    }
                } else if (blockEntity != null){
                    running = true;
                    final var energyStorage = Balm.capabilities().getCapability(blockEntity, direction, CommonCapabilities.ENERGY_STORAGE);
                    running = false;
                    if (energyStorage != null) {
                        return new RebornEnergyStorage(energyStorage);
                    }
                }

                return null;
            }
        });
    }
}
