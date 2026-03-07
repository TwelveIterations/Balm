package net.blay09.mods.balm.fabric.client.internal.color.block;

import net.blay09.mods.balm.client.color.block.internal.AbstractBalmBlockColorRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public class FabricBalmBlockColorRegistrar extends AbstractBalmBlockColorRegistrar {
    public static final FabricBalmBlockColorRegistrar INSTANCE = new FabricBalmBlockColorRegistrar();

    @Override
    public void register(List<BlockTintSource> tintSources, Supplier<Block[]> blocks) {
        BlockColorRegistry.register(tintSources, blocks.get());
    }
}
