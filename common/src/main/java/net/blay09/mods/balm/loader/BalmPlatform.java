package net.blay09.mods.balm.loader;

import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Provides access to mod loader related functions, such as checking if a mod is loaded or accessing jar contents.
 */
public interface BalmPlatform {
    /**
     * Checks whether a mod with the given mod id is loaded.
     *
     * @param modId the mod id to check for.
     * @return true if the mod is loaded.
     */
    boolean isModLoaded(String modId);

    /**
     * Returns a mod info object holding information like the mod's display name and version, if the given mod is loaded.
     *
     * @param modId the mod id to retrieve info for.
     * @return a mod info object, or <code>Optional.empty()</code> if the mod is not loaded.
     */
    Optional<ModInfo> getModInfo(String modId);

    /**
     * @deprecated Use {@link #getModInfo(String)} and {@link ModInfo#name()} instead.
     */
    default String getModName(String modId) {
        return getModInfo(modId).map(ModInfo::name).orElse(modId);
    }

    /**
     * Returns the name of the platform Balm is currently running on.
     *
     * @return the name of the platform Balm is running on.
     * @see net.blay09.mods.balm.api.proxy.LoaderPlatforms
     */
    String name();

    /**
     * Returns the physical side Balm is currently running on - either <code>CLIENT</code> or <code>DEDICATED_SERVER</code>.
     *
     * @return the physical side Balm is running on.
     * @see BalmEnvironment
     */
    BalmEnvironment physicalSide();

    /**
     * Checks whether Minecraft is running as part of a development environment.
     *
     * @return true if this instance is a development environment (i.e. ran from IDE).
     */
    boolean isDevelopmentEnvironment();

    /**
     * Returns a list of all loaded mod ids. This does not include mod id aliases such as Fabric's <code>provides</code>.
     *
     * @return a list of all loaded mod ids.
     */
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

    /**
     * Walks through the given path inside the mod's jar. Useful for accessing resources that aren't assets or data packs.
     *
     * @param modId   the mod id whose jar contents to walk.
     * @param path    the path to walk the jar contents from.
     * @param visitor an implementation of {@link ModResourceVisitor} that will be called for each resource found.
     */
    void visitModResources(String modId, String path, ModResourceVisitor visitor);

    /**
     * Returns a {@link ModResource} for the given path inside the mod's jar, if it exists.
     *
     * @param modId the mod id whose jar contents should be looked up.
     * @param path  the path to the resource to be looked up.
     * @return a {@link ModResource} for the given path in the mod jar, or <code>Optional.empty()</code>.
     */
    Optional<ModResource> lookupModResource(String modId, String path);
}
