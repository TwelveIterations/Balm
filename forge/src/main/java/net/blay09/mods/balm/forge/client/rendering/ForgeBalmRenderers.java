package net.blay09.mods.balm.forge.client.rendering;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public record ForgeBalmRenderers(NamespaceResolver namespaceResolver) implements BalmRenderers {

    @Override
    public ModelLayerLocation registerModel(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        getActiveRegistrations().layerDefinitions.put(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> void registerEntityRenderer(ResourceLocation identifier, Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        getActiveRegistrations().entityRenderers.add(Pair.of(type::get, (EntityRendererProvider<Entity>) provider));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> void registerBlockEntityRenderer(ResourceLocation identifier, Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider) {
        getActiveRegistrations().blockEntityRenderers.add(Pair.of(type::get, (BlockEntityRendererProvider<BlockEntity>) provider));
    }

    @Override
    public void registerBlockColorHandler(ResourceLocation identifier, BlockColor color, Supplier<Block[]> blocks) {
        getActiveRegistrations().blockColors.add(new ColorRegistration<>(color, blocks));
    }

    @Override
    public void setBlockRenderType(Supplier<Block> block, ChunkSectionLayer renderType) {
        getActiveRegistrations().blockRenderTypes.add(new BlockRenderTypeRegistration(block, renderType));
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        getActiveRegistrations().particleProviderFactories.add(new ParticleProviderFactoryRegistration<>(particleType, factory));
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        getActiveRegistrations().particleProviders.add(new ParticleProviderRegistration<>(particleType, provider));
    }

    @Override
    public BalmRenderers scoped(String modId) {
        return new ForgeBalmRenderers(new StaticNamespaceResolver(modId));
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public record BlockRenderTypeRegistration(Supplier<Block> blockSupplier, ChunkSectionLayer renderType) {
    }

    public record ColorRegistration<THandler, TObject>(THandler color, Supplier<TObject[]> objects) {
    }

    public record ParticleProviderFactoryRegistration<T extends ParticleOptions>(Supplier<ParticleType<T>> particleType,
                                                                                  Function<SpriteSet, ParticleProvider<T>> value) {
    }

    public record ParticleProviderRegistration<T extends ParticleOptions>(Supplier<ParticleType<T>> particleType, ParticleProvider<T> value) {
    }

    public static class Registrations {
        public final Map<ModelLayerLocation, Supplier<LayerDefinition>> layerDefinitions = new HashMap<>();
        public final List<Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>>> blockEntityRenderers = new ArrayList<>();
        public final List<Pair<Supplier<EntityType<?>>, EntityRendererProvider<Entity>>> entityRenderers = new ArrayList<>();
        public final List<ColorRegistration<BlockColor, Block>> blockColors = new ArrayList<>();
        public final List<ParticleProviderFactoryRegistration<?>> particleProviderFactories = new ArrayList<>();
        public final List<ParticleProviderRegistration<?>> particleProviders = new ArrayList<>();
        public final List<BlockRenderTypeRegistration> blockRenderTypes = new ArrayList<>();

        @SubscribeEvent
        public void setupClient(FMLClientSetupEvent event) {
            event.enqueueWork(() -> blockRenderTypes.forEach(blockRenderType -> ItemBlockRenderTypes.setRenderLayer(blockRenderType.blockSupplier.get(),
                    blockRenderType.renderType())));
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
