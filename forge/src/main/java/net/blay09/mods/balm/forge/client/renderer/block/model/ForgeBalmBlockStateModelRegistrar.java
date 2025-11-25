package net.blay09.mods.balm.forge.client.renderer.block.model;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.balm.client.renderer.block.model.internal.AbstractBalmBlockStateModelRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

public class ForgeBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar {
    private final ModelEvent.RegisterAdditional event;

    public ForgeBalmBlockStateModelRegistrar(ModelEvent.RegisterAdditional event) {
        this.event = event;
    }

    @Override
    public DeferredBlockStateModel register(ResourceLocation identifier) {
        final var modelResourceLocation = new ModelResourceLocation(identifier, "standalone");
        event.register(modelResourceLocation);
        return new ForgeDeferredBlockStateModel(modelResourceLocation);
    }

    public record ForgeDeferredBlockStateModel(ModelResourceLocation modelResourceLocation) implements DeferredBlockStateModel {
        @Override
        public BakedModel asBlockStateModel() {
            return Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        }
    }
}
