package net.blay09.mods.balm.neoforge.client.rendering;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeBalmRenderers implements BalmRenderers {

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public ModelLayerLocation registerModel(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition) {
        ModelLayerLocation modelLayerLocation = new ModelLayerLocation(location, layer);
        getRegistrations(location.getNamespace()).layerDefinitions.put(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> void registerEntityRenderer(ResourceLocation identifier, Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        getRegistrations(identifier.getNamespace()).entityRenderers.add(Pair.of(type::get, (EntityRendererProvider<Entity>) provider));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> void registerBlockEntityRenderer(ResourceLocation identifier, Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider) {
        getRegistrations(identifier.getNamespace()).blockEntityRenderers.add(Pair.of(type::get, (BlockEntityRendererProvider<BlockEntity>) provider));
    }

    @Override
    public void registerBlockColorHandler(ResourceLocation identifier, BlockColor color, Supplier<Block[]> blocks) {
        getRegistrations(identifier.getNamespace()).blockColors.add(new ColorRegistration<>(color, blocks));
    }

    @Override
    public void setBlockRenderType(Supplier<Block> block, RenderType renderType) {
        // Do nothing in Forge. Forge unfortunately changes the Vanilla model format,
        // so we have to have both this call (for Fabric) and change the JSON (for Forge).
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        getRegistrations(identifier.getNamespace()).particleProviderFactories.add(new ParticleProviderFactoryRegistration<>(particleType, factory));
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        getRegistrations(identifier.getNamespace()).particleProviders.add(new ParticleProviderRegistration<>(particleType, provider));
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

    private record ColorRegistration<THandler, TObject>(THandler color, Supplier<TObject[]> objects) {
    }

    private record ParticleProviderFactoryRegistration<T extends ParticleOptions>(Supplier<ParticleType<T>> particleType,
                                                                                  Function<SpriteSet, ParticleProvider<T>> value) {
    }

    private record ParticleProviderRegistration<T extends ParticleOptions>(Supplier<ParticleType<T>> particleType, ParticleProvider<T> value) {
    }

    private static class Registrations {
        public final Map<ModelLayerLocation, Supplier<LayerDefinition>> layerDefinitions = new HashMap<>();
        public final List<Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>>> blockEntityRenderers = new ArrayList<>();
        public final List<Pair<Supplier<EntityType<?>>, EntityRendererProvider<Entity>>> entityRenderers = new ArrayList<>();
        public final List<ColorRegistration<BlockColor, Block>> blockColors = new ArrayList<>();
        public final List<ParticleProviderFactoryRegistration<?>> particleProviderFactories = new ArrayList<>();
        public final List<ParticleProviderRegistration<?>> particleProviders = new ArrayList<>();

        @SubscribeEvent
        public void setupClient(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public void initRenderers(EntityRenderersEvent.RegisterRenderers event) {
            for (Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>> entry : blockEntityRenderers) {
                event.registerBlockEntityRenderer(entry.getFirst().get(), entry.getSecond());
            }

            for (Pair<Supplier<EntityType<?>>, EntityRendererProvider<Entity>> entry : entityRenderers) {
                event.registerEntityRenderer(entry.getFirst().get(), entry.getSecond());
            }
        }

        @SubscribeEvent
        public void initLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            for (Map.Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : layerDefinitions.entrySet()) {
                event.registerLayerDefinition(entry.getKey(), entry.getValue());
            }
        }

        @SubscribeEvent
        public void initBlockColors(RegisterColorHandlersEvent.Block event) {
            for (ColorRegistration<BlockColor, Block> blockColor : blockColors) {
                event.register(blockColor.color(), blockColor.objects().get());
            }
        }

        @SubscribeEvent
        public void initParticleProviders(RegisterParticleProvidersEvent event) {
            for (final var factory : particleProviderFactories) {
                registerParticleProviderFactory(event, factory);
            }
            for (final var provider : particleProviders) {
                registerParticleProvider(event, provider);
            }
        }

        private <T extends ParticleOptions> void registerParticleProviderFactory(RegisterParticleProvidersEvent event, ParticleProviderFactoryRegistration<T> registration) {
            event.registerSpriteSet(registration.particleType.get(), spriteSet -> registration.value().apply(spriteSet));
        }

        private <T extends ParticleOptions> void registerParticleProvider(RegisterParticleProvidersEvent event, ParticleProviderRegistration<T> registration) {
            event.registerSpriteSet(registration.particleType.get(), spriteSet -> registration.value());
        }
    }
}
