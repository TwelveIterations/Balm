package net.blay09.mods.balm.forge.network;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkChannels {
    private static final Map<String, SimpleChannel> channels = new ConcurrentHashMap<>();
    private static final Map<String, Integer> networkVersions = new ConcurrentHashMap<>();
    private static final Set<String> clientOnlyMods = Sets.newConcurrentHashSet();
    private static final Set<String> serverOnlyMods = Sets.newConcurrentHashSet();

    public static SimpleChannel get(String modId) {
        return channels.computeIfAbsent(modId, key -> {
            ResourceLocation channelName = ResourceLocation.fromNamespaceAndPath(key, "network");
            ChannelBuilder builder = ChannelBuilder.named(channelName);
            final var networkVersion = networkVersions.get(modId);
            if (networkVersion != null) {
                builder = builder.networkProtocolVersion(networkVersion);
            }
            if (serverOnlyMods.contains(modId)) {
                builder = builder.optionalClient();
            }
            if (clientOnlyMods.contains(modId)) {
                builder = builder.optionalServer();
            }
            return builder.simpleChannel();
        });
    }

    public static void allowClientOnly(String modId) {
        clientOnlyMods.add(modId);
    }

    public static void allowServerOnly(String modId) {
        serverOnlyMods.add(modId);
    }

    public static void defineNetworkVersion(String modId, String version) {
        try {
            final var networkVersion = Integer.parseInt(version);
            networkVersions.put(modId, networkVersion);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid network version for mod " + modId + ": Versions must be integers for Forge compatibility.");
        }
    }
}
