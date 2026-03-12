package net.blay09.mods.balm.neoforge.client.color.block.internal;

import net.blay09.mods.balm.client.color.block.internal.AbstractBalmBlockColorRegistrar;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;
import java.util.function.Supplier;

public class NeoForgeBalmBlockColorRegistrar extends AbstractBalmBlockColorRegistrar {
    private final RegisterColorHandlersEvent.BlockTintSources event;

    public NeoForgeBalmBlockColorRegistrar(RegisterColorHandlersEvent.BlockTintSources event) {
        this.event = event;
    }

    @Override
    public void register(List<BlockTintSource> tintSources, Supplier<Block[]> blocks) {
        event.register(tintSources, blocks.get());
    }
}
