package net.blay09.mods.balm.client.renderer.block.model;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractBalmBlockStateModelRegistrar implements BalmBlockStateModelRegistrar {

    @Override
    public <T> Map<T, DeferredBlockStateModel> registerDiscriminated(Set<T> values, Function<T, ResourceLocation> identifierFunction) {
        Map<T, DeferredBlockStateModel> result = new HashMap<>();
        for (T value : values) {
            result.put(value, register(identifierFunction.apply(value)));
        }
        return result;
    }

}
