package net.blay09.mods.balm.api.client.rendering;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRenderers {
    default ModelLayerLocation registerModel(ResourceLocation location, Supplier<LayerDefinition> layerDefinition) {
        return registerModel(location, "main", layerDefinition);
    }

    ModelLayerLocation registerModel(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition);

    default <T extends Entity> void registerEntityRenderer(ResourceLocation identifier, Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        registerEntityRenderer(type, provider);
    }

    default <T extends BlockEntity> void registerBlockEntityRenderer(ResourceLocation identifier, Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider) {
        registerBlockEntityRenderer(type, provider);
    }

    default void registerBlockColorHandler(ResourceLocation identifier, BlockColor color, Supplier<Block[]> blocks) {
        registerBlockColorHandler(color, blocks);
    }

    void registerItemColorHandler(ItemColor color, Supplier<ItemLike[]> items);

    /**
     * @deprecated No-op on Forge. Specify "render_type" in your model as a workaround.
     */
    void setBlockRenderType(Supplier<Block> block, RenderType renderType);

    default <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        registerParticleProvider(particleType, factory);
    }

    default <T extends ParticleOptions> void registerParticleProvider(ResourceLocation identifier, Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        registerParticleProvider(particleType, provider);
    }

    /**
     * @deprecated Use {@link #registerEntityRenderer(ResourceLocation, Supplier, EntityRendererProvider)} instead.
     */
    @Deprecated(since = "1.21.5")
    <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider);

    /**
     * @deprecated Use {@link #registerBlockEntityRenderer(ResourceLocation, Supplier, BlockEntityRendererProvider)} instead.
     */
    @Deprecated(since = "1.21.5")
    <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider);

    /**
     * @deprecated Use {@link #registerBlockColorHandler(ResourceLocation, BlockColor, Supplier)} instead.
     */
    @Deprecated(since = "1.21.5")
    void registerBlockColorHandler(BlockColor color, Supplier<Block[]> blocks);

    /**
     * @deprecated Use {@link #registerParticleProvider(ResourceLocation, Supplier, ParticleProvider)} instead.
     */
    @Deprecated(since = "1.21.5")
    <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory);

    /**
     * @deprecated Use {@link #registerParticleProvider(ResourceLocation, Supplier, Function)} instead.
     */
    @Deprecated(since = "1.21.5")
    <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider);

    BalmRenderers scoped(String modId);
}
