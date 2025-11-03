package net.blay09.mods.balm.api.stats;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import java.util.function.Function;

public abstract class AbstractBalmCustomStatFactoryImpl implements BalmCustomStatFactory {
    private final BalmRegistrar registrar;
    protected final String namespace;

    public AbstractBalmCustomStatFactoryImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public ResourceLocation register(String name, StatFormatter formatter) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.CUSTOM_STAT, resourceLocation);
        registrar.register(resourceKey, Function.identity());
        return resourceLocation;
    }
}
