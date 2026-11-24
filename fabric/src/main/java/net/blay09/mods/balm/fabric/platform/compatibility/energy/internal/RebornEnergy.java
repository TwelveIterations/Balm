package net.blay09.mods.balm.fabric.platform.compatibility.energy.internal;

import net.blay09.mods.balm.platform.transfer.TransactionalEnergyStorage;
import net.blay09.mods.balm.fabric.platform.transfer.internal.FabricTransactionalEnergyStorage;
import net.blay09.mods.balm.fabric.internal.FabricBalm;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.platform.capabilities.CommonCapabilities;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import java.util.Objects;

public class RebornEnergy {
    public static TransactionalEnergyStorage createStorage(long capacity, Runnable onChange) {
        return new FabricTransactionalEnergyStorage(
                new SimpleEnergyStorage(capacity, capacity, capacity) {
                    @Override
                    protected void onFinalCommit() { onChange.run(); }
                });
    }

    public RebornEnergy() {
        FabricBalm.registerTransactionalStorage(EnergyStorage.SIDED, CommonCapabilities.TRANSACTIONAL_ENERGY_STORAGE,
                FabricTransactionalEnergyStorage::new,
                FabricTransactionalEnergyStorage::unwrap);

        EnergyStorage.SIDED.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            private boolean running;

            @Override
            public @Nullable EnergyStorage find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
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
                    final var energyStorage = Balm.capabilities().getCapability(blockEntity, direction, Objects.requireNonNull(CommonCapabilities.ENERGY_STORAGE));
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
