package net.blay09.mods.balm.client.platform.config.internal;

import net.blay09.mods.balm.client.platform.config.BalmCustomConfigControlRegistrar;
import net.blay09.mods.balm.client.platform.config.ConfigControl;
import net.minecraft.resources.Identifier;

public class BalmCustomConfigControlRegistrarImpl implements BalmCustomConfigControlRegistrar {
    private final String namespace;

    public BalmCustomConfigControlRegistrarImpl(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public <T> void register(String path, ConfigControl<T> control) {
        register(Identifier.fromNamespaceAndPath(namespace, path), control);
    }

    @Override
    public <T> void register(Identifier identifier, ConfigControl<T> control) {
        ConfigControlRegistry.register(identifier, control);
    }
}
