package net.blay09.mods.balm.fabric.client.rendering;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated
public record FabricBalmRenderers(NamespaceResolver namespaceResolver) implements BalmRenderers {
    @Override
    public ModelLayerLocation registerModel(Identifier location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinition::get);
        return modelLayerLocation;
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(Identifier identifier, Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        EntityRendererRegistry.register(type.get(), provider);
    }

    @Override
    public <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void registerBlockEntityRenderer(Identifier identifier, Supplier<BlockEntityType<TBlockEntity>> type, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider) {
        BlockEntityRenderers.register(type.get(), provider);
    }

    @Override
    public void registerBlockColorHandler(Identifier identifier, BlockColor color, Supplier<Block[]> blocks) {
        ColorProviderRegistry.BLOCK.register(color, blocks.get());
    }

    @Override
    public void setBlockRenderType(Supplier<Block> block, ChunkSectionLayer chunkSectionLayer) {
        BlockRenderLayerMap.putBlock(block.get(), chunkSectionLayer);
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(Identifier identifier, Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        ParticleFactoryRegistry.getInstance().register(particleType.get(), factory::apply);
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(Identifier identifier, Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        ParticleFactoryRegistry.getInstance().register(particleType.get(), provider);
    }

    @Override
    public BalmRenderers scoped(String modId) {
        return new FabricBalmRenderers(new StaticNamespaceResolver(modId));
    }
}
