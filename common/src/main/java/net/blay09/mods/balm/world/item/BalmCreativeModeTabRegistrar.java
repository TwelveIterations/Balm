package net.blay09.mods.balm.world.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Provides convenience access to registering creative tabs.
 */
public interface BalmCreativeModeTabRegistrar {

    default BalmCreativeModeTabRegistration register(String name, Function<CreativeModeTab.Builder, CreativeModeTab.Builder> builderConsumer) {
        return register(name, (id, builder) -> builderConsumer.apply(builder));
    }

    BalmCreativeModeTabRegistration register(String name, BiFunction<Identifier, CreativeModeTab.Builder, CreativeModeTab.Builder> creativeModeTab);

    CreativeModeTab.Builder createBuilder();
}
