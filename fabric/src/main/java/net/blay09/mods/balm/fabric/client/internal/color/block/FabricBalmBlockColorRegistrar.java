package net.blay09.mods.balm.fabric.client.internal.color.block;

import net.blay09.mods.balm.client.color.block.internal.AbstractBalmBlockColorRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class FabricBalmBlockColorRegistrar extends AbstractBalmBlockColorRegistrar {
    public static final FabricBalmBlockColorRegistrar INSTANCE = new FabricBalmBlockColorRegistrar();

    @Override
    public void register(BlockColor color, Supplier<Block[]> blocks) {
        BlockColorRegistry.register(color, blocks.get());
    }
}
