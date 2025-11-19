package net.blay09.mods.balm.neoforge.platform.internal;

import net.blay09.mods.balm.platform.BalmEnvironment;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.platform.LoaderPlatforms;
import net.blay09.mods.balm.platform.ModInfo;
import net.blay09.mods.balm.neoforge.platform.resources.internal.NeoForgeModResource;
import net.blay09.mods.balm.platform.resources.ModResource;
import net.blay09.mods.balm.platform.resources.ModResourceVisitor;
import net.minecraft.SharedConstants;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class NeoForgeBalmPlatform implements BalmPlatform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public Optional<ModInfo> getModInfo(String modId) {
        return ModList.get().getModContainerById(modId).map(NeoForgeModInfo::new);
    }

    @Override
    public BalmEnvironment physicalSide() {
        return switch (FMLEnvironment.getDist()) {
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
        return LoaderPlatforms.NEOFORGE;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return SharedConstants.IS_RUNNING_IN_IDE;
    }

    @Override
    public void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        final var modFile = ModList.get().getModFileById(modId);
        if (modFile != null) {
            modFile.getFile().getContents().visitContent(path, (relativePath, resource) -> visitor.visit(new NeoForgeModResource(relativePath, resource.retain())));
        }
    }

    @Override
    public Optional<ModResource> lookupModResource(String modId, String path) {
        return Optional.ofNullable(ModList.get().getModFileById(modId))
                .map(it -> it.getFile().getContents().get(path))
                .map(it -> new NeoForgeModResource(path, it.retain()));
    }
}
