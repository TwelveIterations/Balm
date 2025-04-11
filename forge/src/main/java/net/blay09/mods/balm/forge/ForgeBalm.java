package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.blay09.mods.balm.common.config.ExampleDeclarativeConfig;
import net.blay09.mods.balm.common.config.ExampleReflectionConfig;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.capability.ForgeCommonCapabilities;
import net.blay09.mods.balm.forge.client.ForgeBalmClient;
import net.blay09.mods.balm.forge.energy.ForgeEnergyStorage;
import net.blay09.mods.balm.forge.fluid.ForgeFluidTank;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.function.BiFunction;

@Mod("balm")
public class ForgeBalm {

    public ForgeBalm(FMLJavaModLoadingContext context) {
        final var modEventBus = context.getModEventBus();
        BalmLoadContexts.register("balm", new ForgeLoadContext(modEventBus));

        Balm.registerModule(new ForgeCommonCapabilities());
        ((ForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        Balm.getConfig().registerConfig(ExampleDeclarativeConfig.schema);
        Balm.getConfig().registerConfig(ExampleReflectionConfig.class);

        ForgeBalmWorldGen.initializeBalmBiomeModifiers(modEventBus);
        modEventBus.addListener(ForgeBalmClient::onInitializeClient);

        final var capabilities = (ForgeBalmCapabilities) Balm.getCapabilities();
        final var nativeItemHandler = capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "item_handler"), IItemHandler.class, ForgeCapabilities.ITEM_HANDLER);
        final var nativeFluidHandler = capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "fluid_handler"), IFluidHandler.class, ForgeCapabilities.FLUID_HANDLER);
        final var nativeEnergyStorage = capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "energy_storage"), IEnergyStorage.class, ForgeCapabilities.ENERGY);

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
                                return new ForgeFluidTank(fluidTank);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var fluidTank = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.FLUID_TANK);
                            running = false;
                            if (fluidTank != null) {
                                return new ForgeFluidTank(fluidTank);
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
                                return new ForgeEnergyStorage(energyStorage);
                            }
                        } else if (blockEntity != null) {
                            running = true;
                            final var energyStorage = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.ENERGY_STORAGE);
                            running = false;
                            if (energyStorage != null) {
                                return new ForgeEnergyStorage(energyStorage);
                            }
                        }

                        return null;
                    }
                });

        ((ForgeBalmCapabilities) Balm.getCapabilities()).register("balm", context.getModEventBus());
    }
}
