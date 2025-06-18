package net.blay09.mods.balm.forge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ForgeBalmModels(NamespaceResolver namespaceResolver) implements BalmModels {

    @Override
    public DeferredObject<BlockStateModel> loadModel(ResourceLocation identifier) {
        final var registrations = getActiveRegistrations();
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

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    @Override
    public BalmModels scoped(String modId) {
        return new ForgeBalmModels(new StaticNamespaceResolver(modId));
    }

    public static class Registrations {
        public final List<ResourceLocation> extraModels = new ArrayList<>();
        public final Map<ResourceLocation, StateDefinition<Block, BlockState>> extraStateDefinitions = new HashMap<>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterModelStateDefinitions event) {
            extraModels.forEach(it -> {
                final var stateDefinition = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).create(Block::defaultBlockState, BlockState::new);
                event.register(it, stateDefinition);
                extraStateDefinitions.put(it, stateDefinition);
            });
        }
    }
}
