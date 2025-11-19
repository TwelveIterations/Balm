package net.blay09.mods.balm.client.renderer.block.model.internal;

import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractBalmBlockStateModelRegistrar implements BalmBlockStateModelRegistrar {

    @Override
    public <T> Map<@Nullable T, DeferredBlockStateModel> registerDiscriminated(Set<@Nullable T> values, Function<@Nullable T, Identifier> identifierFunction) {
        Map<@Nullable T, DeferredBlockStateModel> result = new HashMap<>();
        for (T value : values) {
            result.put(value, register(identifierFunction.apply(value)));
        }
        return result;
    }

}
