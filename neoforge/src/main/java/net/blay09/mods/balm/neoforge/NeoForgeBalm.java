package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.blay09.mods.balm.neoforge.capability.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.compat.hudinfo.TheOneProbeModCompat;
import net.blay09.mods.balm.neoforge.energy.NeoForgeEnergyStorage;
import net.blay09.mods.balm.neoforge.fluid.NeoForgeFluidTank;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import java.util.function.BiFunction;

@Mod("balm")
public class NeoForgeBalm {

    public NeoForgeBalm(IEventBus modBus) {
        BalmLoadContexts.register("balm", new NeoForgeLoadContext(modBus));

        ((NeoForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        DeferredRegisters.register("balm", modBus);
        ModBusEventRegisters.register("balm", modBus);

        NeoForgeBalmWorldGen.initializeBalmBiomeModifiers(modBus);
        modBus.addListener(this::enqueueIMC);

        NeoForgeBalmCapabilities capabilities = (NeoForgeBalmCapabilities) Balm.getCapabilities();
        final var nativeItemHandler = capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "item_handler"),
                Capabilities.ItemHandler.BLOCK);
        final var nativeFluidHandler = capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "fluid_handler"),
                Capabilities.FluidHandler.BLOCK);
        final var nativeEnergyStorage = capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "energy_storage"),
                Capabilities.EnergyStorage.BLOCK);

        capabilities.registerFallbackBlockEntityProvider(ResourceLocation.fromNamespaceAndPath("balm", "item_handler"),
                nativeItemHandler,
                new BiFunction<>() {
                    private boolean running;

                    @Override
                    public IItemHandler apply(BlockEntity blockEntity, Direction direction) {
                        if (running) {
                            return null;
                        }

                        if (blockEntity instanceof BalmContainerProvider containerProvider) {
                            final var container = direction != null ? containerProvider.getContainer(direction) : containerProvider.getContainer();
                            if (container != null) {
                                return new InvWrapper(container);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var container = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.CONTAINER);
                            running = false;
                            if (container != null) {
                                return new InvWrapper(container);
                            }
                        }

                        return null;
                    }
                });

        capabilities.registerFallbackBlockEntityProvider(ResourceLocation.fromNamespaceAndPath("balm", "fluid_handler"),
                nativeFluidHandler,
                new BiFunction<>() {
                    private boolean running;

                    @Override
                    public IFluidHandler apply(BlockEntity blockEntity, Direction direction) {
                        if (running) {
                            return null;
                        }

                        if (blockEntity instanceof BalmFluidTankProvider fluidTankProvider) {
                            final var fluidTank = direction != null ? fluidTankProvider.getFluidTank(direction) : fluidTankProvider.getFluidTank();
                            if (fluidTank != null) {
                                return new NeoForgeFluidTank(fluidTank);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var fluidTank = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.FLUID_TANK);
                            running = false;
                            if (fluidTank != null) {
                                return new NeoForgeFluidTank(fluidTank);
                            }
                        }

                        return null;
                    }
                });

        capabilities.registerFallbackBlockEntityProvider(ResourceLocation.fromNamespaceAndPath("balm", "energy_storage"),
                nativeEnergyStorage,
                new BiFunction<>() {
                    private boolean running;

                    @Override
                    public IEnergyStorage apply(BlockEntity blockEntity, Direction direction) {
                        if (running) {
                            return null;
                        }

                        if (blockEntity instanceof BalmEnergyStorageProvider energyStorageProvider) {
                            final var energyStorage = direction != null ? energyStorageProvider.getEnergyStorage(direction) : energyStorageProvider.getEnergyStorage();
                            if (energyStorage != null) {
                                return new NeoForgeEnergyStorage(energyStorage);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var energyStorage = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.ENERGY_STORAGE);
                            running = false;
                            if (energyStorage != null) {
                                return new NeoForgeEnergyStorage(energyStorage);
                            }
                        }

                        return null;
                    }
                });
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded("theoneprobe")) {
            TheOneProbeModCompat.register();
        }
    }

}
