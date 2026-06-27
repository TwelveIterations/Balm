package net.blay09.mods.balm.client.platform.config;

import net.minecraft.resources.Identifier;

public class BalmCustomConfigControlRegistrar {
    private final String namespace;

    public BalmCustomConfigControlRegistrar(String namespace) {
        this.namespace = namespace;
    }

    public <T> void register(String path, ConfigControl<T> control) {
        register(Identifier.fromNamespaceAndPath(namespace, path), control);
    }

    public <T> void register(Identifier identifier, ConfigControl<T> control) {
        ConfigControlRegistry.register(identifier, control);
    }
}
