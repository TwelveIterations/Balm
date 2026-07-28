package net.blay09.mods.balm.world.level.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Set;
import java.util.function.*;

/**
 * Provides convenience access to registering blocks and block items.
 */
public interface BalmBlockRegistrar {

    default BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesBuilder) {
        return register(name, constructor, () -> propertiesBuilder.apply(BlockBehaviour.Properties.of()));
    }

    default BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, BlockBehaviour.Properties properties) {
        return register(name, constructor, () -> properties);
    }

    BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, Supplier<BlockBehaviour.Properties> propertiesSupplier);

    void addAlias(ResourceLocation oldId, ResourceLocation newId);

    void addAlias(String oldName, String newName);

    default <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(T[] values, Function<T, String> nameFunction, BiFunction<T, BlockBehaviour.Properties, Block> constructor, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesSupplier) {
        return registerDiscriminated(Set.of(values), nameFunction, constructor, propertiesSupplier);
    }

    default <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(T[] values, Function<T, String> nameFunction, BiFunction<T, BlockBehaviour.Properties, Block> constructor, BiFunction<T, BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesSupplier) {
        return registerDiscriminated(Set.of(values), nameFunction, constructor, propertiesSupplier);
    }

    default <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(Set<T> values, Function<T, String> nameFunction, BiFunction<T, BlockBehaviour.Properties, Block> constructor, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesSupplier) {
        return registerDiscriminated(values, nameFunction, constructor, (discriminator, properties) -> propertiesSupplier.apply(properties));
    }

    <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(Set<T> values, Function<T, String> nameFunction, BiFunction<T, BlockBehaviour.Properties, Block> constructor, BiFunction<T, BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesSupplier);

}
