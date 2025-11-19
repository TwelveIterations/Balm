package net.blay09.mods.balm.neoforge.client.renderer.block.model.internal;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.balm.client.renderer.block.model.internal.AbstractBalmBlockStateModelRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.Objects;

public class NeoForgeBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar {

    private final ModelEvent.RegisterStandalone event;

    public NeoForgeBalmBlockStateModelRegistrar(ModelEvent.RegisterStandalone event) {
        this.event = event;
    }

    @Override
    public DeferredBlockStateModel register(Identifier identifier) {
        final var standaloneModelKey = new StandaloneModelKey<BlockStateModel>(identifier::toString);
        event.register(standaloneModelKey, SimpleUnbakedStandaloneModel.blockStateModel(identifier));
        return new NeoForgeDeferredBlockStateModel(standaloneModelKey);
    }

    public record NeoForgeDeferredBlockStateModel(StandaloneModelKey<BlockStateModel> key) implements DeferredBlockStateModel {
        @Override
        public BlockStateModel asBlockStateModel() {
            return Objects.requireNonNull(Minecraft.getInstance().getModelManager().getStandaloneModel(key));
        }
    }
}
