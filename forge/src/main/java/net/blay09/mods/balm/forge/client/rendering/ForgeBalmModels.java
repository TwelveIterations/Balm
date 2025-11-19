package net.blay09.mods.balm.forge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.forge.ModBusEventRegister;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public record ForgeBalmModels() implements BalmModels {

    @Override
    public DeferredObject<BlockStateModel> loadModel(Identifier identifier) {
        final var registrations = getRegistrations(identifier.getNamespace());
        final var deferredModel = new DeferredObject<BlockStateModel>(identifier) {
            @Override
            public BlockStateModel resolve() {
                final var modelManager = Minecraft.getInstance().getModelManager();
                final var stateDefinition = registrations.extraStateDefinitions.get(identifier);
                return modelManager.getBlockModelShaper().getBlockModel(stateDefinition.any());
            }

            @Override
            public boolean canResolve() {
                return registrations.extraStateDefinitions.containsKey(identifier);
            }
        };
        registrations.extraModels.add(identifier);
        return deferredModel;
    }

    private Registrations getRegistrations(String namespace) {
        return ModBusEventRegisters.getRegistrations(namespace, Registrations.class);
    }

    public static class Registrations implements ModBusEventRegister {
        public final List<Identifier> extraModels = new ArrayList<>();
        public final Map<Identifier, StateDefinition<Block, BlockState>> extraStateDefinitions = new HashMap<>();

        private void onRegisterAdditionalModels(ModelEvent.RegisterModelStateDefinitions event) {
            extraModels.forEach(it -> {
                final var stateDefinition = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).create(Block::defaultBlockState, BlockState::new);
                event.register(it, stateDefinition);
                extraStateDefinitions.put(it, stateDefinition);
            });
        }

        @Override
        public void register(BusGroup busGroup) {
            ModelEvent.RegisterModelStateDefinitions.BUS.addListener(this::onRegisterAdditionalModels);
        }
    }
}
