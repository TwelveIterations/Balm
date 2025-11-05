package net.blay09.mods.balm.client.renderer.block.model;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface BalmBlockStateModelRegistrar {
    DeferredBlockStateModel register(ResourceLocation identifier);

    default <T> Map<T, DeferredBlockStateModel> registerDiscriminated(T[] values, Function<T, ResourceLocation> identifierFunction) {
        return registerDiscriminated(Set.of(values), identifierFunction);
    }

    <T> Map<T, DeferredBlockStateModel> registerDiscriminated(Set<@Nullable T> values, Function<T, ResourceLocation> identifierFunction);
}
