package net.blay09.mods.balm.forge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ForgeBalmModels implements BalmModels {

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public DeferredObject<BlockStateModel> loadModel(ResourceLocation identifier) {
        final var deferredModel = new DeferredObject<BlockStateModel>(identifier) {
            @Override
            public BlockStateModel resolve() {
                return getRegistrations(identifier.getNamespace()).bakedExtraModels.get(identifier);
            }

            @Override
            public boolean canResolve() {
                return getRegistrations(identifier.getNamespace()).bakedExtraModels.containsKey(identifier);
            }
        };
        getRegistrations(identifier.getNamespace()).extraModels.add(identifier);
        return deferredModel;
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

    private static class Registrations {
        public final List<ResourceLocation> extraModels = new ArrayList<>();
        public final Map<ResourceLocation, StateDefinition<Block, BlockState>> extraStateDefinitions = new HashMap<>();
        public final Map<ResourceLocation, BlockStateModel> bakedExtraModels = new HashMap<>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterModelStateDefinitions event) {
            extraModels.forEach(it -> {
                final var stateDefinition = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).create(Block::defaultBlockState, BlockState::new);
                event.register(it, stateDefinition);
                extraStateDefinitions.put(it, stateDefinition);
            });
        }

        @SubscribeEvent
        public void onBakingCompleted(ModelEvent.BakingCompleted event) {
            final var modelManager = event.getModelManager();
            extraStateDefinitions.forEach((id, stateDefinition) -> {
                final var model = modelManager.getBlockModelShaper().getBlockModel(stateDefinition.any());
                bakedExtraModels.put(id, model);
            });
        }
    }
}
