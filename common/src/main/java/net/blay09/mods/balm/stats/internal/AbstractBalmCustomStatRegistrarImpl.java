package net.blay09.mods.balm.stats.internal;

import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;

import java.util.function.Function;

public abstract class AbstractBalmCustomStatRegistrarImpl implements BalmCustomStatRegistrar {
    private final BalmRegistrar registrar;
    protected final String namespace;

    public AbstractBalmCustomStatRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public ResourceLocation register(String name, StatFormatter formatter) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.CUSTOM_STAT, resourceLocation);
        // Importantly, the ResourceLocation we register must be identical (not just equal) to the one that we return
        registrar.register(resourceKey, () -> resourceLocation);
        return resourceLocation;
    }
}
