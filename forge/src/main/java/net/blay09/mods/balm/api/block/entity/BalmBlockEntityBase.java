package net.blay09.mods.balm.api.block.entity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.forge.container.BalmInvWrapper;
import net.blay09.mods.balm.forge.energy.ForgeEnergyStorage;
import net.blay09.mods.balm.forge.fluid.ForgeFluidTank;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class BalmBlockEntityBase extends BlockEntity {

    private final Map<Capability<?>, LazyOptional<?>> capabilities = new HashMap<>();
    private final Table<Capability<?>, Direction, LazyOptional<?>> sidedCapabilities = HashBasedTable.create();
    private boolean capabilitiesInitialized;

    public BalmBlockEntityBase(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    /**
     * @deprecated See {@link net.blay09.mods.balm.api.capability.BalmCapabilities}
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    private void addCapabilities(BalmProvider<?> provider, Map<Capability<?>, LazyOptional<?>> capabilities) {
        ForgeBalmProviders forgeProviders = (ForgeBalmProviders) Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(provider.getProviderClass());
        capabilities.put(capability, LazyOptional.of(provider::getInstance));

        if (provider.getProviderClass() == Container.class) {
            capabilities.put(ForgeCapabilities.ITEM_HANDLER, LazyOptional.of(() -> new BalmInvWrapper((Container) provider.getInstance())));
        } else if (provider.getProviderClass() == FluidTank.class) {
            capabilities.put(ForgeCapabilities.FLUID_HANDLER, LazyOptional.of(() -> new ForgeFluidTank((FluidTank) provider.getInstance())));
        } else if (provider.getProviderClass() == EnergyStorage.class) {
            capabilities.put(ForgeCapabilities.ENERGY, LazyOptional.of(() -> new ForgeEnergyStorage((EnergyStorage) provider.getInstance())));
        }
    }

    /**
     * @deprecated See {@link net.blay09.mods.balm.api.capability.BalmCapabilities}
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    @SuppressWarnings("unchecked")
    public <T> T getProvider(Class<T> clazz) {
        ForgeBalmProviders forgeProviders = (ForgeBalmProviders) Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(clazz);
        return (T) getCapability(capability).resolve().orElse(null);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities#registerProvider(ResourceLocation, CapabilityType, BiFunction, Supplier)} instead.
     */
    @NotNull
    @Override
    @Deprecated(forRemoval = true, since = "1.21.5")
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!capabilitiesInitialized) {
            List<Object> providers = new ArrayList<>();
            buildProviders(providers);

            for (Object holder : providers) {
                BalmProviderHolder providerHolder = (BalmProviderHolder) holder;
                for (BalmProvider<?> provider : providerHolder.getProviders()) {
                    addCapabilities(provider, capabilities);
                }

                for (Pair<Direction, BalmProvider<?>> pair : providerHolder.getSidedProviders()) {
                    Direction direction = pair.getFirst();
                    BalmProvider<?> provider = pair.getSecond();
                    Map<Capability<?>, LazyOptional<?>> sidedCapabilities = this.sidedCapabilities.column(direction);
                    addCapabilities(provider, sidedCapabilities);
                }
            }
            capabilitiesInitialized = true;
        }

        LazyOptional<?> result = null;
        if (side != null) {
            result = sidedCapabilities.get(cap, side);
        }
        if (result == null) {
            result = capabilities.get(cap);
        }
        return result != null ? result.cast() : super.getCapability(cap, side);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities#registerProvider(ResourceLocation, CapabilityType, BiFunction, Supplier)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    protected abstract void buildProviders(List<Object> providers);

}
