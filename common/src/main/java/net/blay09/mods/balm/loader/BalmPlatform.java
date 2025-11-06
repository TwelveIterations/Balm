package net.blay09.mods.balm.loader;

import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface BalmPlatform {
    boolean isModLoaded(String modId);

    String getModName(String modId);

    String name();

    BalmEnvironment physicalSide();

    boolean isDevelopmentEnvironment();

    List<String> loadedPrimaryModIds();

    /**
     * Returns the server on dedicated servers, or the integrated server in singleplayer.
     * <p>
     * Always <code>null</code> when connected to a server in multiplayer.
     *
     * @return the server instance, or <code>null</code> if no dedicated or integrated server is running
     */
    @Nullable
    MinecraftServer server();

    void visitModResources(String modId, String path, ModResourceVisitor visitor);

    Optional<ModResource> lookupModResource(String modId, String path);
}
