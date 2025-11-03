package net.blay09.mods.balm.fabric.loader;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.blay09.mods.balm.api.proxy.LoaderPlatforms;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FabricBalmPlatform implements BalmPlatform {

    private final AtomicReference<MinecraftServer> currentServer = new AtomicReference<>();

    public void initialize() {
        Balm.events().onEvent(ServerStartedEvent.class, event -> currentServer.set(event.getServer()));
        Balm.events().onEvent(ServerStoppedEvent.class, event -> currentServer.set(null));
    }

    @Override
    public BalmEnvironment physicalSide() {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> BalmEnvironment.CLIENT;
            case SERVER -> BalmEnvironment.DEDICATED_SERVER;
        };
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public List<String> loadedPrimaryModIds() {
        return FabricLoader.getInstance().getAllMods().stream()
                .map(it -> it.getMetadata().getId())
                .toList();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return FabricLoader.getInstance().getModContainer(modId).map(it -> it.getMetadata().getName()).orElse(modId);
    }

    @Override
    public String name() {
        return LoaderPlatforms.FABRIC;
    }

    @Override
    public @Nullable MinecraftServer server() {
        return currentServer.get();
    }
}
