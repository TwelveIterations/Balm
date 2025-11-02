package net.blay09.mods.balm.forge.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.*;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class ForgeBalmResources implements BalmResources {
    @Override
    public <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        final var register = DeferredRegisters.get(ForgeRegistries.CONDITION_SERIALIZERS.getKey(), identifier.getNamespace());
        register.register(identifier.getPath(),
                () -> codec.xmap(it -> new ForgeBalmResourceCondition<>(identifier, it, id -> ForgeRegistries.CONDITION_SERIALIZERS.get().getValue(id)),
                        ForgeBalmResourceCondition::delegate));
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
