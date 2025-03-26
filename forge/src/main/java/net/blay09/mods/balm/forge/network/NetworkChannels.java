package net.blay09.mods.balm.forge.network;

import net.blay09.mods.balm.api.network.NetworkVersions;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class NetworkChannels {
    private static final String version = "1.0"; // all mods will just have same version for now until I clean this up
    private static final Map<String, SimpleChannel> channels = new ConcurrentHashMap<>();
    private static final Map<String, Predicate<String>> acceptedClientVersions = new ConcurrentHashMap<>();
    private static final Map<String, Predicate<String>> acceptedServerVersions = new ConcurrentHashMap<>();

    public static SimpleChannel get(String modId) {
        return channels.computeIfAbsent(modId, key -> {
            ResourceLocation channelName = new ResourceLocation(key, "network");
            return NetworkRegistry.newSimpleChannel(channelName,
                    () -> version,
                    it -> acceptedClientVersions.getOrDefault(modId, NetworkChannels::defaultVersionCheck).test(it),
                    it -> acceptedServerVersions.getOrDefault(modId, NetworkChannels::defaultVersionCheck).test(it));
        });
    }

    public static void allowClientOnly(String modId) {
        acceptedClientVersions.put(modId, it -> true);
    }

    public static void allowServerOnly(String modId) {
        acceptedServerVersions.put(modId, it -> true);
    }

    private static boolean defaultVersionCheck(String it) {
        return it.equals(version);
    }

    public static void defineNetworkVersion(String modId, String version) {
        // NOOP because I can't be bothered backporting this
    }
}
