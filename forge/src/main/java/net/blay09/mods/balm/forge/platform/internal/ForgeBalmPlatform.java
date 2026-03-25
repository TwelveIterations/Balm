package net.blay09.mods.balm.forge.platform.internal;

import net.blay09.mods.balm.platform.BalmEnvironment;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.platform.LoaderPlatforms;
import net.blay09.mods.balm.platform.ModInfo;
import net.blay09.mods.balm.platform.resources.ModResource;
import net.blay09.mods.balm.platform.resources.ModResourceVisitor;
import net.blay09.mods.balm.platform.resources.internal.PathModResource;
import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class ForgeBalmPlatform implements BalmPlatform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.isLoaded(modId);
    }

    @Override
    public Optional<ModInfo> getModInfo(String modId) {
        return ModList.getModContainerById(modId).map(ForgeModInfo::new);
    }

    @Override
    public BalmEnvironment physicalSide() {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> BalmEnvironment.CLIENT;
            case DEDICATED_SERVER -> BalmEnvironment.DEDICATED_SERVER;
        };
    }

    @Override
    public List<String> loadedPrimaryModIds() {
        return ModList.getMods().stream().map(IModInfo::getModId).toList();
    }

    @Override
    public @Nullable MinecraftServer server() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public String name() {
        return LoaderPlatforms.FORGE;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return SharedConstants.IS_RUNNING_IN_IDE;
    }

    @Override
    public void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        final var modFile = ModList.getModFileById(modId);
        final var nioPath = modFile.getFile().findResource(path);
        if (Files.exists(nioPath)) {
            try (final var walker = Files.walk(nioPath)) {
                walker.forEach(childPath -> visitor.visit(new PathModResource(childPath)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<ModResource> lookupModResource(String modId, String path) {
        final var modFile = ModList.getModFileById(modId);
        final var resource = modFile.getFile().findResource(path);
        return Files.exists(resource) ? Optional.of(new PathModResource(resource)) : Optional.empty();
    }
}
