package com.example.balm;

import net.blay09.mods.balm.core.BalmRegistrars;
import net.minecraft.resources.Identifier;

public class BalmExample {
    public static final String MOD_ID = "balm_example";

    public static void initialize(BalmRegistrars registrars) {
        registrars.registerModule(new CustomRegistryTestModule());
        registrars.registerModule(new EntityCapabilityTestModule());
        registrars.registerModule(new MiscTestModule());
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
