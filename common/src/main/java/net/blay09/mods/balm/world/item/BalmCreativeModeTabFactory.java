package net.blay09.mods.balm.world.item;

import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides convenience access to registering creative tabs.
 */
public interface BalmCreativeModeTabFactory {

    default BalmCreativeModeTabRegistration register(String name, Function<CreativeModeTab.Builder, CreativeModeTab.Builder> builderConsumer) {
        return register(name, () -> builderConsumer.apply(createBuilder()).build());
    }

    default BalmCreativeModeTabRegistration register(String name, CreativeModeTab creativeModeTab) {
        return register(name, () -> creativeModeTab);
    }

    BalmCreativeModeTabRegistration register(String name, Supplier<CreativeModeTab> creativeModeTab);

    CreativeModeTab.Builder createBuilder();
}
