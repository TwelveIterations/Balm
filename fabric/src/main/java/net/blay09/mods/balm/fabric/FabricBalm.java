package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.entity.BalmEntity;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.network.NetworkVersions;
import net.blay09.mods.balm.api.network.ServerboundModListMessage;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.blay09.mods.balm.common.command.BalmCommand;
import net.blay09.mods.balm.common.config.ConfigSync;
import net.blay09.mods.balm.common.config.ExampleDeclarativeConfig;
import net.blay09.mods.balm.common.config.ExampleReflectionConfig;
import net.blay09.mods.balm.common.resources.ConfigResourceCondition;
import net.blay09.mods.balm.fabric.fluid.BalmFluidStorage;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

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
        ConfigSync.initialize();
        Balm.getConfig().registerConfig(ExampleDeclarativeConfig.schema);
        Balm.getConfig().registerConfig(ExampleReflectionConfig.class);
        Balm.getCommands().register(BalmCommand::register);

        Balm.getNetworking().defineNetworkVersion("balm", "2");

        Balm.getResources().registerResourceCondition(ResourceLocation.fromNamespaceAndPath("balm", "config"), ConfigResourceCondition.CODEC);

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag data = ((BalmEntity) oldPlayer).getFabricBalmData();
            ((BalmEntity) newPlayer).setFabricBalmData(data);
        });

        CommonCapabilities.initialize(Balm.getCapabilities());

        ItemStorage.SIDED.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            private boolean running;

            @Override
            public @Nullable Storage<ItemVariant> find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
                if (running) {
                    return null;
                }

                if (blockEntity instanceof BalmContainerProvider containerProvider) {
                    final var container = direction != null ? containerProvider.getContainer(direction) : containerProvider.getContainer();
                    if (container != null) {
                        return InventoryStorage.of(container, direction);
                    }
                } else if (blockEntity != null) {
                    running = true;
                    final var container = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.CONTAINER);
                    running = false;
                    if (container != null) {
                        return InventoryStorage.of(container, direction);
                    }
                }

                return null;
            }
        });

        FluidStorage.SIDED.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            private boolean running;

            @Override
            public @Nullable Storage<FluidVariant> find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
                if (running) {
                    return null;
                }

                if (blockEntity instanceof BalmFluidTankProvider fluidTankProvider) {
                    final var fluidTank = direction != null ? fluidTankProvider.getFluidTank(direction) : fluidTankProvider.getFluidTank();
                    if (fluidTank != null) {
                        return new BalmFluidStorage(fluidTank);
                    }
                } else if (blockEntity != null) {
                    running = true;
                    final var fluidTank = Balm.getCapabilities().getCapability(blockEntity, direction, CommonCapabilities.FLUID_TANK);
                    running = false;
                    if (fluidTank != null) {
                        return new BalmFluidStorage(fluidTank);
                    }
                }

                return null;
            }
        });

        Balm.getNetworking().registerServerboundPacket(ServerboundModListMessage.TYPE,
                ServerboundModListMessage.class, StreamCodec.of((buf, message) -> {
                    buf.writeVarInt(message.modList().size());
                    message.modList().forEach((modId, versions) -> {
                        buf.writeUtf(modId);
                        buf.writeUtf(versions.modVersion());
                        buf.writeUtf(versions.networkVersion());
                    });
                }, (buf) -> {
                    final var modVersions = new HashMap<String, NetworkVersions>();
                    final var modCount = buf.readVarInt();
                    for (int i = 0; i < modCount; i++) {
                        String modId = buf.readUtf();
                        modVersions.put(modId, new NetworkVersions(buf.readUtf(), buf.readUtf()));
                    }
                    return new ServerboundModListMessage(modVersions);
                }), (player, message) -> {
                    final var networking = (FabricBalmNetworking) Balm.getNetworking();
                    for (final var entry : message.modList().entrySet()) {
                        final var modId = entry.getKey();
                        if (networking.isClientOnly(modId) || networking.isServerOnly(modId)) {
                            continue;
                        }

                        final var clientVersions = entry.getValue();
                        final var clientNetworkVersion = clientVersions.networkVersion();
                        final var serverVersionsOpt = networking.getNetworkVersions(modId);
                        if (serverVersionsOpt.isEmpty()) {
                            player.connection.disconnect(Component.translatable("disconnect.balm.mod_missing_on_server",
                                    Component.literal(modId).withStyle(ChatFormatting.RED)));
                            return;
                        }

                        final var serverVersions = serverVersionsOpt.get();
                        if (!clientNetworkVersion.equals(serverVersions.networkVersion())) {
                            final var clientModVersion = clientVersions.modVersion();
                            final var serverModVersion = serverVersions.modVersion();
                            player.connection.disconnect(Component.translatable("disconnect.balm.mod_version_mismatch",
                                    Component.literal(modId).withStyle(ChatFormatting.GOLD),
                                    Component.literal(serverModVersion).withStyle(ChatFormatting.GREEN),
                                    Component.literal(clientModVersion).withStyle(ChatFormatting.RED)));
                            return;
                        }
                    }

                    for (final var modId : networking.getRegisteredMods()) {
                        if (!networking.isServerOnly(modId) && !networking.isClientOnly(modId)) {
                            if (!message.modList().containsKey(modId)) {
                                networking.getNetworkVersions(modId).ifPresent(serverVersions -> {
                                    final var serverModVersion = serverVersions.modVersion();
                                    player.connection.disconnect(Component.translatable("disconnect.balm.mod_missing_on_client",
                                            Component.literal(modId).withStyle(ChatFormatting.RED),
                                            Component.literal(modId).withStyle(ChatFormatting.GOLD),
                                            Component.literal(serverModVersion).withStyle(ChatFormatting.GREEN)));
                                });
                            }
                        }
                    }
                });

        Balm.initializeIfLoaded("team_reborn_energy", "net.blay09.mods.balm.fabric.compat.energy.RebornEnergy");
    }
}
