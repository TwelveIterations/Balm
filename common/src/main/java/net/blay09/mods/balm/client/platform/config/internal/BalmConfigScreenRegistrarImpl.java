package net.blay09.mods.balm.client.platform.config.internal;

import net.blay09.mods.balm.client.platform.config.BalmConfigScreenFactory;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenRegistrar;
import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;

public class BalmConfigScreenRegistrarImpl implements BalmConfigScreenRegistrar {

    private final String namespace;

    public BalmConfigScreenRegistrarImpl(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void register(BalmConfigScreenFactory factory) {
        BalmConfigScreenProviders.registerModOverride(namespace, factory);
    }
}
