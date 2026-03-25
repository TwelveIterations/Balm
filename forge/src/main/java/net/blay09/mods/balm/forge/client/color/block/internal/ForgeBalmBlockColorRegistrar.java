package net.blay09.mods.balm.forge.client.color.block.internal;

import net.blay09.mods.balm.client.color.block.internal.AbstractBalmBlockColorRegistrar;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.List;
import java.util.function.Supplier;

public class ForgeBalmBlockColorRegistrar extends AbstractBalmBlockColorRegistrar {
    private final RegisterColorHandlersEvent.Block event;

    public ForgeBalmBlockColorRegistrar(RegisterColorHandlersEvent.Block event) {
        this.event = event;
    }

    @Override
    public void register(List<BlockTintSource> color, Supplier<Block[]> blocks) {
        event.register(color, blocks.get());
    }
}
