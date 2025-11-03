package net.blay09.mods.balm.world.item.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.BiFunction;

public abstract class AbstractBalmCreativeModeTabFactory implements BalmCreativeModeTabFactory {

    private final BalmRegistrar registrar;
    private final String namespace;

    protected AbstractBalmCreativeModeTabFactory(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public BalmCreativeModeTabRegistration register(String name, BiFunction<ResourceLocation, CreativeModeTab.Builder, CreativeModeTab.Builder> constructor) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, resourceLocation);
        final var holder = registrar.register(resourceKey, (id) -> constructor.apply(id, createBuilder()).build());
        return new BalmCreativeModeTabRegistrationImpl(holder);
    }

    private record BalmCreativeModeTabRegistrationImpl(Holder<CreativeModeTab> holder) implements BalmCreativeModeTabRegistration {
        @Override
        public Holder<CreativeModeTab> asHolder() {
            return holder;
        }
    }
}
