package net.blay09.mods.balm.fabric.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.internal.FabricBalmProxy;
import net.blay09.mods.balm.fabric.network.internal.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.blay09.mods.balm.fabric.platform.runtime.internal.FabricBalmRuntime;
import net.blay09.mods.balm.platform.BalmEnvironment;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.nbt.BalmDataHolder;
import net.blay09.mods.balm.platform.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.network.protocol.common.custom.internal.NetworkVersions;
import net.blay09.mods.balm.network.protocol.common.custom.internal.ServerboundModListMessage;
import net.blay09.mods.balm.platform.runtime.internal.BalmLoadContexts;
import net.blay09.mods.balm.platform.capabilities.CommonCapabilities;
import net.blay09.mods.balm.fabric.platform.fluid.internal.BalmFluidStorage;
import net.blay09.mods.balm.fabric.platform.internal.FabricBalmPlatform;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

public class FabricBalm implements ModInitializer {

    private static final Supplier<FabricBalmProxy> proxy = Balm.<FabricBalmProxy>sidedProxy("net.blay09.mods.balm.fabric.platform.internalFabricBalmProxy",
            "net.blay09.mods.balm.fabric.client.internal.platform.internal.FabricBalmClientProxy")
            .buildLazily();

    public static FabricBalmProxy getProxy() {
        return proxy.get();
    }

    @Override
    public void onInitialize() {
        BalmLoadContexts.register("balm", FabricLoadContext.INSTANCE);

        ((FabricBalmRuntime) Balm.getRuntime()).initializeRuntime();

        ((FabricBalmPlatform) Balm.platform()).initialize();

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            CompoundTag data = ((BalmDataHolder) oldPlayer).balm$getFabricBalmData();
            ((BalmDataHolder) newPlayer).balm$setFabricBalmData(data);
        });

        ItemStorage.SIDED.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            private boolean running;

            @Override
            public @Nullable Storage<ItemVariant> find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
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
                    final var container = Balm.capabilities().getCapability(blockEntity, direction, Objects.requireNonNull(CommonCapabilities.CONTAINER));
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
            public @Nullable Storage<FluidVariant> find(Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
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
                    final var fluidTank = Balm.capabilities().getCapability(blockEntity, direction, Objects.requireNonNull(CommonCapabilities.FLUID_TANK));
                    running = false;
                    if (fluidTank != null) {
                        return new BalmFluidStorage(fluidTank);
                    }
                }

                return null;
            }
        });

        Balm.networking().registerServerboundPacket(ServerboundModListMessage.TYPE,
                ServerboundModListMessage.class, StreamCodec.of((buf, message) -> {
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
                }), (player, message) -> {
                    final var networking = (FabricBalmNetworking) Balm.networking();
                    for (final var entry : message.modList().entrySet()) {
                        final var modId = entry.getKey();
                        final var clientVersions = entry.getValue();
                        final var serverVersionsOpt = networking.getNetworkVersions(modId, BalmEnvironment.DEDICATED_SERVER);
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
                        final var serverVersions = networking.getNetworkVersions(modId, BalmEnvironment.DEDICATED_SERVER).orElseThrow();
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

        Balm.initializeIfLoaded("team_reborn_energy", "net.blay09.mods.balm.fabric.platform.compatibility.energy.internal.RebornEnergy");
    }
}
