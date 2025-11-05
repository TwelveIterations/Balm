package net.blay09.mods.balm.forge.client.renderer.block.model;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.balm.client.renderer.block.model.AbstractBalmBlockStateModelRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.client.event.ModelEvent;

public class ForgeBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar {
    private final ModelEvent.RegisterModelStateDefinitions event;

    public ForgeBalmBlockStateModelRegistrar(ModelEvent.RegisterModelStateDefinitions event) {
        this.event = event;
    }

    @Override
    public DeferredBlockStateModel register(ResourceLocation identifier) {
        final var stateDefinition = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).create(Block::defaultBlockState, BlockState::new);
        event.register(identifier, stateDefinition);
        return new ForgeDeferredBlockStateModel(stateDefinition);
    }

    public record ForgeDeferredBlockStateModel(
            StateDefinition<Block, BlockState> stateDefinition) implements DeferredBlockStateModel {
        @Override
        public BlockStateModel asBlockStateModel() {
            return Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(stateDefinition.any());
        }
    }
}
