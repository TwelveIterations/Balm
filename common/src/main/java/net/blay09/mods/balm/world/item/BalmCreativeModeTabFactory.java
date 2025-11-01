package net.blay09.mods.balm.world.item;

import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides convenience access to registering creative tabs.
 */
public interface BalmCreativeModeTabFactory {

    default BalmCreativeModeTabRegistration register(String location, Function<CreativeModeTab.Builder, CreativeModeTab.Builder> builderConsumer) {
        return register(location, () -> builderConsumer.apply(createBuilder()).build());
    }

    default BalmCreativeModeTabRegistration register(String location, CreativeModeTab creativeModeTab) {
        return register(location, () -> creativeModeTab);
    }

    BalmCreativeModeTabRegistration register(String location, Supplier<CreativeModeTab> creativeModeTab);

    CreativeModeTab.Builder createBuilder();
}
