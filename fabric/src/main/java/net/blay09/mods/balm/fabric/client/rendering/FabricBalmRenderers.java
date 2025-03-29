package net.blay09.mods.balm.fabric.client.rendering;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricBalmRenderers implements BalmRenderers {
    @Override
    public ModelLayerLocation registerModel(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinition::get);
        return modelLayerLocation;
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        EntityRendererRegistry.register(type.get(), provider);
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider) {
        BlockEntityRenderers.register(type.get(), provider);
    }

    @Override
    public void registerBlockColorHandler(BlockColor color, Supplier<Block[]> blocks) {
        ColorProviderRegistry.BLOCK.register(color, blocks.get());
    }

    @Override
    public void setBlockRenderType(Supplier<Block> block, RenderType renderType) {
        BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType);
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        ParticleFactoryRegistry.getInstance().register(particleType.get(), factory::apply);
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        ParticleFactoryRegistry.getInstance().register(particleType.get(), provider);
    }
}
