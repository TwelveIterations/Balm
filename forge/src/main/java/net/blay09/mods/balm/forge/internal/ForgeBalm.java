package net.blay09.mods.balm.forge.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.forge.platform.runtime.internal.ForgeBalmRuntime;
import net.blay09.mods.balm.forge.platform.runtime.ForgeLoadContext;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegisters;
import net.blay09.mods.balm.forge.core.internal.DeferredRegisters;
import net.blay09.mods.balm.forge.platform.capabilities.internal.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.platform.capabilities.internal.ForgeCommonCapabilities;
import net.blay09.mods.balm.forge.client.internal.ForgeBalmClient;
import net.blay09.mods.balm.forge.platform.energy.internal.ForgeEnergyStorage;
import net.blay09.mods.balm.forge.platform.fluid.internal.ForgeFluidTank;
import net.blay09.mods.balm.forge.world.level.levelgen.internal.ForgeBalmWorldGen;
import net.blay09.mods.balm.platform.capabilities.CommonCapabilities;
import net.blay09.mods.balm.platform.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.platform.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.platform.runtime.internal.BalmLoadContexts;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.function.BiFunction;

@Mod("balm")
public class ForgeBalm {

    public ForgeBalm(FMLJavaModLoadingContext context) {
        final var modBusGroup = context.getModBusGroup();
        BalmLoadContexts.register("balm", new ForgeLoadContext(modBusGroup));

        Balm.getRuntime().registerModule(new BalmRegistrars(Balm.getRuntime(), "balm"), new ForgeCommonCapabilities());
        ((ForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        DeferredRegisters.register("balm", modBusGroup);
        ModBusEventRegisters.register("balm", modBusGroup);

        ForgeBalmWorldGen.initializeBalmBiomeModifiers(modBusGroup);
        FMLClientSetupEvent.getBus(modBusGroup).addListener(ForgeBalmClient::onInitializeClient);

        final var capabilities = (ForgeBalmCapabilities) Balm.capabilities();
        final var nativeItemHandler = capabilities.addExistingType(Identifier.fromNamespaceAndPath("forge", "item_handler"), IItemHandler.class, ForgeCapabilities.ITEM_HANDLER);
        final var nativeFluidHandler = capabilities.addExistingType(Identifier.fromNamespaceAndPath("forge", "fluid_handler"), IFluidHandler.class, ForgeCapabilities.FLUID_HANDLER);
        final var nativeEnergyStorage = capabilities.addExistingType(Identifier.fromNamespaceAndPath("forge", "energy_storage"), IEnergyStorage.class, ForgeCapabilities.ENERGY);

        capabilities.registerFallbackBlockEntityProvider(Identifier.fromNamespaceAndPath("balm", "item_handler"),
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
                            final var container = Balm.capabilities().getCapability(blockEntity, direction, CommonCapabilities.CONTAINER);
                            running = false;
                            if (container != null) {
                                return new InvWrapper(container);
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
                    public IFluidHandler apply(BlockEntity blockEntity, Direction direction) {
                        if (running) {
                            return null;
                        }

                        if (blockEntity instanceof BalmFluidTankProvider fluidTankProvider) {
                            final var fluidTank = direction != null ? fluidTankProvider.getFluidTank(direction) : fluidTankProvider.getFluidTank();
                            if (fluidTank != null) {
                                return new ForgeFluidTank(fluidTank);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var fluidTank = Balm.capabilities().getCapability(blockEntity, direction, CommonCapabilities.FLUID_TANK);
                            running = false;
                            if (fluidTank != null) {
                                return new ForgeFluidTank(fluidTank);
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
                    public IEnergyStorage apply(BlockEntity blockEntity, Direction direction) {
                        if (running) {
                            return null;
                        }

                        if (blockEntity instanceof BalmEnergyStorageProvider energyStorageProvider) {
                            final var energyStorage = direction != null ? energyStorageProvider.getEnergyStorage(direction) : energyStorageProvider.getEnergyStorage();
                            if (energyStorage != null) {
                                return new ForgeEnergyStorage(energyStorage);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var energyStorage = Balm.capabilities().getCapability(blockEntity, direction, CommonCapabilities.ENERGY_STORAGE);
                            running = false;
                            if (energyStorage != null) {
                                return new ForgeEnergyStorage(energyStorage);
                            }
                        }

                        return null;
                    }
                });
    }
}
