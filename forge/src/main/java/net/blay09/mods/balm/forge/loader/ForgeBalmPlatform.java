package net.blay09.mods.balm.forge.loader;

import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.proxy.LoaderPlatforms;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.api.resources.PathModResource;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.platform.ModInfo;
import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class ForgeBalmPlatform implements BalmPlatform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return ModList.get().getModContainerById(modId).map(it -> it.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public Optional<ModInfo> getModInfo(String modId) {
        return ModList.get().getModContainerById(modId).map(ForgeModInfo::new);
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
        return ModList.get().getMods().stream().map(IModInfo::getModId).toList();
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
        final var modFile = ModList.get().getModFileById(modId);
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
        final var modFile = ModList.get().getModFileById(modId);
        final var resource = modFile.getFile().findResource(path);
        return Files.exists(resource) ? Optional.of(new PathModResource(resource)) : Optional.empty();
    }
}
