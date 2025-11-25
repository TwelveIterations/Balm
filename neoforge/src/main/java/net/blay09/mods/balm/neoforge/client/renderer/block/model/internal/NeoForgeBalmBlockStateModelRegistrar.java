package net.blay09.mods.balm.neoforge.client.renderer.block.model.internal;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.balm.client.renderer.block.model.internal.AbstractBalmBlockStateModelRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.Objects;

public class NeoForgeBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar {

    private final ModelEvent.RegisterAdditional event;

    public NeoForgeBalmBlockStateModelRegistrar(ModelEvent.RegisterAdditional event) {
        this.event = event;
    }

    @Override
    public DeferredBlockStateModel register(ResourceLocation identifier) {
        final var standaloneModelKey = new ModelResourceLocation(identifier, "standalone");
        event.register(standaloneModelKey);
        return new NeoForgeDeferredBlockStateModel(standaloneModelKey);
    }

    public record NeoForgeDeferredBlockStateModel(ModelResourceLocation key) implements DeferredBlockStateModel {
        @Override
        public BakedModel asBlockStateModel() {
            return Objects.requireNonNull(Minecraft.getInstance().getModelManager().getModel(key));
        }
    }
}
