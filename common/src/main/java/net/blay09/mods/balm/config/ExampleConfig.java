package net.blay09.mods.balm.config;

import net.blay09.mods.balm.api.Balm;

public class ExampleConfig {
    public static ExampleConfigData getActive() {
        return Balm.getConfig().getActive(ExampleConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(ExampleConfigData.class, null);
    }
}
