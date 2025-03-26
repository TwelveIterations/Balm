package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.entity.BalmEntity;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.network.NetworkVersions;
import net.blay09.mods.balm.api.network.ServerboundModListMessage;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.common.command.BalmCommand;
import net.blay09.mods.balm.config.ExampleConfig;
import net.blay09.mods.balm.fabric.fluid.BalmFluidStorage;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.provider.FabricBalmProviders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;

import java.util.HashMap;

import static net.blay09.mods.balm.api.Balm.sidedProxy;

public class FabricBalm implements ModInitializer {

    private static final SidedProxy<FabricBalmProxy> proxy = sidedProxy("net.blay09.mods.balm.fabric.FabricBalmProxy",
            "net.blay09.mods.balm.fabric.client.FabricBalmClientProxy");

    public static FabricBalmProxy getProxy() {
        return proxy.get();
    }

    @Override
    public void onInitialize() {
        ((FabricBalmRuntime) Balm.getRuntime()).initializeRuntime();

        ((FabricBalmHooks) Balm.getHooks()).initialize();
        ((AbstractBalmConfig) Balm.getConfig()).initialize();
        ExampleConfig.initialize();
        Balm.getCommands().register(BalmCommand::register);

        Balm.getNetworking().defineNetworkVersion("balm", "2");

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag data = ((BalmEntity) oldPlayer).getFabricBalmData();
            ((BalmEntity) newPlayer).setFabricBalmData(data);
        });

        var providers = ((FabricBalmProviders) Balm.getProviders());
        providers.registerProvider(new ResourceLocation("balm", "container"), Container.class);
        providers.registerProvider(new ResourceLocation("balm", "fluid_tank"), FluidTank.class);
        providers.registerProvider(new ResourceLocation("balm", "energy_storage"), EnergyStorage.class);

        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof BalmContainerProvider containerProvider) {
                final var container = containerProvider.getContainer(direction);
                if (container != null) {
                    return InventoryStorage.of(container, direction);
                }
            }

            return null;
        });

        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof BalmFluidTankProvider fluidTankProvider) {
                final var fluidTank = fluidTankProvider.getFluidTank(direction);
                if (fluidTank != null) {
                    return new BalmFluidStorage(fluidTank);
                }
            }

            return null;
        });

        Balm.getNetworking().registerServerboundPacket(new ResourceLocation("balm", "mod_list"),
                ServerboundModListMessage.class, (message, buf) -> {
                    buf.writeVarInt(message.modList().size());
                    message.modList().forEach((modId, versions) -> {
                        buf.writeUtf(modId);
                        buf.writeUtf(versions.modVersion());
                        buf.writeUtf(versions.networkVersion());
                        buf.writeBoolean(versions.requireRemote());
                    });
                }, (buf) -> {
                    final var modVersions = new HashMap<String, NetworkVersions>();
                    final var modCount = buf.readVarInt();
                    for (int i = 0; i < modCount; i++) {
                        String modId = buf.readUtf();
                        modVersions.put(modId, new NetworkVersions(buf.readUtf(), buf.readUtf(), buf.readBoolean()));
                    }
                    return new ServerboundModListMessage(modVersions);
                }, (player, message) -> {
                    final var networking = (FabricBalmNetworking) Balm.getNetworking();
                    for (final var entry : message.modList().entrySet()) {
                        final var modId = entry.getKey();
                        final var clientVersions = entry.getValue();
                        final var serverVersionsOpt = networking.getNetworkVersions(modId, BalmEnvironment.SERVER);
                        if (serverVersionsOpt.isEmpty()) {
                            if (clientVersions.requireRemote()) {
                                player.connection.disconnect(Component.translatable("disconnect.balm.mod_missing_on_server",
                                        Component.literal(modId).withStyle(ChatFormatting.RED)));
                                return;
                            } else {
                                continue;
                            }
                        }

                        final var serverVersions = serverVersionsOpt.get();
                        if (!clientVersions.networkVersion().equals(serverVersions.networkVersion())) {
                            player.connection.disconnect(Component.translatable("disconnect.balm.mod_version_mismatch",
                                    Component.literal(modId).withStyle(ChatFormatting.GOLD),
                                    Component.literal(serverVersions.modVersion()).withStyle(ChatFormatting.GREEN),
                                    Component.literal(clientVersions.modVersion()).withStyle(ChatFormatting.RED)));
                            return;
                        }
                    }

                    for (final var modId : networking.getRegisteredMods()) {
                        final var serverVersions = networking.getNetworkVersions(modId, BalmEnvironment.SERVER).orElseThrow();
                        if (serverVersions.requireRemote() && !message.modList().containsKey(modId)) {
                            final var serverModVersion = serverVersions.modVersion();
                            player.connection.disconnect(Component.translatable("disconnect.balm.mod_missing_on_client",
                                    Component.literal(modId).withStyle(ChatFormatting.RED),
                                    Component.literal(modId).withStyle(ChatFormatting.GOLD),
                                    Component.literal(serverModVersion).withStyle(ChatFormatting.GREEN)));
                            return;
                        }
                    }
                });

        Balm.initializeIfLoaded("team_reborn_energy", "net.blay09.mods.balm.fabric.compat.energy.RebornEnergy");
    }
}
