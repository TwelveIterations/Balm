package net.blay09.mods.balm.platform.module.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.network.internal.CommonBalmNetworking;
import net.blay09.mods.balm.platform.BalmEnvironment;
import net.blay09.mods.balm.platform.event.BidirectionalEventMapper;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.blay09.mods.balm.platform.event.callback.ServerPlayerCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RemotePlayerModList {
    
    private static final Map<UUID, Map<String, NetworkVersions>> REMOTE_MOD_LISTS = new ConcurrentHashMap<>();

    public record RemotePlayerModListReceived(ServerPlayer player, Map<String, NetworkVersions> modList) {}

    public static BidirectionalEventMapper<Consumer<RemotePlayerModListReceived>> RECEIVED = EventMapper.createBound(RemotePlayerModListReceived.class);
    
    private RemotePlayerModList() {
    }

    public static void initialize() {
        ServerPlayerCallback.Leave.EVENT.register(player -> REMOTE_MOD_LISTS.remove(player.getUUID()));
    }

    public static void store(ServerPlayer player, Map<String, NetworkVersions> modList) {
        REMOTE_MOD_LISTS.put(player.getUUID(), new ConcurrentHashMap<>(modList));
        RECEIVED.invoker().accept(new RemotePlayerModListReceived(player, modList));
    }

    public static boolean wasModAnnouncedToServer(ServerPlayer player, String modId) {
        final var clientMods = REMOTE_MOD_LISTS.get(player.getUUID());
        return clientMods != null && clientMods.containsKey(modId);
    }

    public static void validateRemoteMods(ServerPlayer player, Map<String, NetworkVersions> modList) {
        final var networking = (CommonBalmNetworking) Balm.networking();
        for (final var entry : modList.entrySet()) {
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
            if (serverVersions.requireRemote() && !modList.containsKey(modId)) {
                final var serverModVersion = serverVersions.modVersion();
                player.connection.disconnect(Component.translatable("disconnect.balm.mod_missing_on_client",
                        Component.literal(modId).withStyle(ChatFormatting.RED),
                        Component.literal(modId).withStyle(ChatFormatting.GOLD),
                        Component.literal(serverModVersion).withStyle(ChatFormatting.GREEN)));
                return;
            }
        }
    }
}
