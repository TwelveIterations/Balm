package net.blay09.mods.balm.forge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
import java.util.stream.Collectors;

public record ForgeBalmModels(NamespaceResolver namespaceResolver) implements BalmModels {

    @Override
    public DeferredObject<BakedModel> loadModel(ResourceLocation identifier) {
        final var registrations = getActiveRegistrations();
        final var deferredModel = new DeferredObject<BakedModel>(identifier) {
            @Override
            public BakedModel resolve() {
                return registrations.bakedStandaloneModels.get(identifier);
            }

            @Override
            public boolean canResolve() {
                return registrations.bakedStandaloneModels.containsKey(identifier);
            }
        };
        registrations.additionalModels.add(identifier);
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
        public final List<ResourceLocation> additionalModels = new ArrayList<>();
        public Map<ResourceLocation, BakedModel> bakedStandaloneModels = new HashMap<>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterModelStateDefinitions event) {
            additionalModels.forEach(it ->
                    event.register(it, new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).create(Block::defaultBlockState, BlockState::new)));
        }

        @SubscribeEvent
        public void onBakingCompleted(ModelEvent.BakingCompleted event) {
            final var modelManager = event.getModelManager();
            bakedStandaloneModels = additionalModels.stream()
                    .collect(Collectors.toMap(it -> it, it -> modelManager.getModel(new ModelResourceLocation(it, ""))));
        }
    }
}
