package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides convenience access to registering blocks and block items.
 */
public interface BalmBlockFactory {

    default BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesBuilder) {
        return register(name, constructor, () -> propertiesBuilder.apply(BlockBehaviour.Properties.of()));
    }

    default BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, BlockBehaviour.Properties properties) {
        return register(name, constructor, () -> properties);
    }

    BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, Supplier<BlockBehaviour.Properties> propertiesSupplier);

}
