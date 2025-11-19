package net.blay09.mods.balm.stats.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;

public abstract class AbstractBalmCustomStatRegistrarImpl implements BalmCustomStatRegistrar {
    private final BalmRegistrar registrar;
    protected final String namespace;

    public AbstractBalmCustomStatRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public final Identifier register(String name, StatFormatter formatter) {
        return register(Identifier.fromNamespaceAndPath(namespace, name), formatter);
    }

    @Override
    public Identifier register(Identifier statIdentifier, StatFormatter formatter) {
        final var resourceKey = ResourceKey.create(Registries.CUSTOM_STAT, statIdentifier);
        // Importantly, the Identifier we register must be identical (not just equal) to the one that we return
        registrar.register(resourceKey, () -> statIdentifier);
        return statIdentifier;
    }

}
