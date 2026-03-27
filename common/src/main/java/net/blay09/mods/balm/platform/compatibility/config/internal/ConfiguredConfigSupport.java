package net.blay09.mods.balm.platform.compatibility.config.internal;

import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;

public class ConfiguredConfigSupport {
    public ConfiguredConfigSupport() {
        BalmConfigScreenProviders.register("configured", ConfiguredConfigProvider::getConfigScreenFactory);
    }
}
