package com.example.balm;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;

public class BalmExample {
    public static void initialize(BalmRegistrars registrars) {
        Balm.config().registerConfig(ExampleConfig.class);
        System.out.println("Hello common");
    }
}
