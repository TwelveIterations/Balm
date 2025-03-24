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
    ModelLayerLocation registerModel(ResourceLocation location, Supplier<LayerDefinition> layerDefinition);

    <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider);

    <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider);

    void registerBlockColorHandler(BlockColor color, Supplier<Block[]> blocks);

    void registerItemColorHandler(ItemColor color, Supplier<ItemLike[]> items);

    /**
     * @deprecated Works fine on Fabric, but on Forge and NeoForge they changed the vanilla format with no hook (sigh).
     */
    @Deprecated
    void setBlockRenderType(Supplier<Block> block, RenderType renderType);

    <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory);

    <T extends ParticleOptions> void registerParticleProvider(Supplier<ParticleType<T>> particleType, ParticleProvider<T> provider);
}
