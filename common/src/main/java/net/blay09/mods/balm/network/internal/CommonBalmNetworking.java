package net.blay09.mods.balm.network.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.balm.platform.BalmEnvironment;
import net.blay09.mods.balm.platform.ModInfo;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.*;

public abstract class CommonBalmNetworking implements BalmNetworking {
    protected final Set<String> registeredMods = Collections.synchronizedSet(new HashSet<>());
    protected final Set<String> clientOnlyMods = Collections.synchronizedSet(new HashSet<>());
    protected final Set<String> serverOnlyMods = Collections.synchronizedSet(new HashSet<>());
    protected final Map<String, String> networkVersions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void allowClientOnly(String modId) {
        clientOnlyMods.add(modId);
    }

    @Override
    public void allowServerOnly(String modId) {
        serverOnlyMods.add(modId);
    }

    @Override
    public void defineNetworkVersion(String modId, String version) {
        networkVersions.put(modId, version);
    }

    @Nullable
    public String getNetworkVersion(String modId) {
        return networkVersions.get(modId);
    }

    public Optional<NetworkVersions> getNetworkVersions(String modId, BalmEnvironment environment) {
        return Balm.platform().getModInfo(modId)
                .map(ModInfo::versionString)
                .map(modVersion -> {
                    final var networkVersion = networkVersions.getOrDefault(modId, modVersion);
                    return new NetworkVersions(modVersion, networkVersion, environment == BalmEnvironment.CLIENT ? !isClientOnly(modId) : !isServerOnly(modId));
                });
    }

    public Set<String> getRegisteredMods() {
        return registeredMods;
    }

    public boolean isClientOnly(String modId) {
        return clientOnlyMods.contains(modId);
    }

    public boolean isServerOnly(String modId) {
        return serverOnlyMods.contains(modId);
    }

    public boolean isMessageSupported(ServerPlayer player, CustomPacketPayload payload) {
        return RemotePlayerModList.wasModAnnouncedToServer(player, payload.type().id().getNamespace());
    }

    public abstract boolean isMessageSupportedByServer(CustomPacketPayload payload);
}
