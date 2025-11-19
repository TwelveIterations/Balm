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
import net.minecraft.resources.Identifier;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import org.jspecify.annotations.Nullable;

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
        final var nativeItemHandler = capabilities.addExistingType(Identifier.fromNamespaceAndPath("neoforge", "item_handler"),
                Capabilities.Item.BLOCK);
        final var nativeFluidHandler = capabilities.addExistingType(Identifier.fromNamespaceAndPath("neoforge", "fluid_handler"),
                Capabilities.Fluid.BLOCK);
        final var nativeEnergyStorage = capabilities.addExistingType(Identifier.fromNamespaceAndPath("neoforge", "energy_storage"),
                Capabilities.Energy.BLOCK);

        capabilities.registerFallbackBlockEntityProvider(Identifier.fromNamespaceAndPath("balm", "item_handler"),
                nativeItemHandler,
                new BiFunction<>() {
                    private boolean running;

                    @Override
                    public ResourceHandler<ItemResource> apply(BlockEntity blockEntity, Direction direction) {
                        if (running) {
                            return null;
                        }

                        if (blockEntity instanceof BalmContainerProvider containerProvider) {
                            final var container = direction != null ? containerProvider.getContainer(direction) : containerProvider.getContainer();
                            if (container instanceof WorldlyContainer worldlyContainer) {
                                return new WorldlyContainerWrapper(worldlyContainer, direction);
                            } else if (container != null) {
                                return VanillaContainerWrapper.of(container);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var container = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.CONTAINER);
                            running = false;
                            if (container instanceof WorldlyContainer worldlyContainer) {
                                return new WorldlyContainerWrapper(worldlyContainer, direction);
                            } else if (container != null) {
                                return VanillaContainerWrapper.of(container);
                            }
                        }

                        return null;
                    }
                });

        capabilities.registerFallbackBlockEntityProvider(Identifier.fromNamespaceAndPath("balm", "fluid_handler"),
                nativeFluidHandler,
                new BiFunction<>() {
                    private boolean running;

                    @Override
                    public ResourceHandler<FluidResource> apply(BlockEntity blockEntity, Direction direction) {
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

        capabilities.registerFallbackBlockEntityProvider(Identifier.fromNamespaceAndPath("balm", "energy_storage"),
                nativeEnergyStorage,
                new BiFunction<>() {
                    private boolean running;

                    @Override
                    public EnergyHandler apply(BlockEntity blockEntity, @Nullable Direction direction) {
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
