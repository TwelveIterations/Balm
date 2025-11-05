package net.blay09.mods.balm.neoforge.client.color.block;

import net.blay09.mods.balm.client.color.block.internal.AbstractBalmBlockColorRegistrar;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.function.Supplier;

public class NeoForgeBalmBlockColorRegistrar extends AbstractBalmBlockColorRegistrar {
    private final RegisterColorHandlersEvent.Block event;

    public NeoForgeBalmBlockColorRegistrar(RegisterColorHandlersEvent.Block event) {
        this.event = event;
    }

    @Override
    public void register(BlockColor color, Supplier<Block[]> blocks) {
        event.register(color, blocks.get());
    }
}
