package net.blay09.mods.balm.neoforge.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

public class NeoForgeBalmResources implements BalmResources {
    @Override
    public <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        final var register = DeferredRegisters.get(NeoForgeRegistries.CONDITION_SERIALIZERS, identifier.getNamespace());
        register.register(identifier.getPath(),
                () -> codec.xmap(it -> new NeoForgeBalmResourceCondition<>(identifier, it, NeoForgeRegistries.CONDITION_SERIALIZERS::getValue),
                        NeoForgeBalmResourceCondition::delegate));
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
