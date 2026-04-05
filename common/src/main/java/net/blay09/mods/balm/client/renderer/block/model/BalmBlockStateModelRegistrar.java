package net.blay09.mods.balm.client.renderer.block.model;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface BalmBlockStateModelRegistrar {
    DeferredBlockStateModel register(Identifier identifier);

    default <T> Map<T, DeferredBlockStateModel> registerDiscriminated(T[] values, Function<T, Identifier> identifierFunction) {
        return registerDiscriminated(Set.of(values), identifierFunction);
    }

    <T> Map<T, DeferredBlockStateModel> registerDiscriminated(Set<T> values, Function<T, Identifier> identifierFunction);
}
