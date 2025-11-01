package net.blay09.mods.balm.world.item;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public abstract class AbstractBalmCreativeModeTabFactory implements BalmCreativeModeTabFactory {

    private final BalmRegistrar registrar;
    private final String namespace;

    protected AbstractBalmCreativeModeTabFactory(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public BalmCreativeModeTabRegistration register(String location, Supplier<CreativeModeTab> creativeModeTab) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, location);
        final var resourceKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, resourceLocation);
        final var holder = registrar.register(resourceKey, creativeModeTab);
        return new BalmCreativeModeTabRegistrationImpl(holder);
    }

    private record BalmCreativeModeTabRegistrationImpl(Holder<CreativeModeTab> holder) implements BalmCreativeModeTabRegistration {
        @Override
        public Holder<CreativeModeTab> asHolder() {
            return holder;
        }
    }
}
