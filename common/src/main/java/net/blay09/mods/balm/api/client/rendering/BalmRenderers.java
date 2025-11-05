package net.blay09.mods.balm.api.client.rendering;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRenderers {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.client.BalmClient#modelLayers(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    default ModelLayerLocation registerModel(ResourceLocation location, Supplier<LayerDefinition> layerDefinition) {
        return registerModel(location, "main", layerDefinition);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.client.BalmClient#modelLayers(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    ModelLayerLocation registerModel(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.client.BalmClient#entityRenderers(String, Consumer)} instead.
     */
    @Deprecated
    <T extends Entity> void registerEntityRenderer(ResourceLocation id, Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.client.BalmClient#blockEntityRenderers(String, Consumer)} instead.
     */
    @Deprecated
    <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void registerBlockEntityRenderer(ResourceLocation id, Supplier<BlockEntityType<TBlockEntity>> type, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.client.BalmClient#blockColors(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    void registerBlockColorHandler(ResourceLocation id, BlockColor color, Supplier<Block[]> blocks);

    /**
     * @deprecated No-op on NeoForge. Specify "render_type" in your model as a workaround.
     */
    @Deprecated
    void setBlockRenderType(Supplier<Block> block, ChunkSectionLayer chunkSectionLayer);

    <T extends ParticleOptions> void registerParticleProvider(ResourceLocation id, Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory);

    <T extends ParticleOptions> void registerParticleProvider(ResourceLocation id, Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider);

    default BalmRenderers scoped(String modId) {
        return this;
    }
}
