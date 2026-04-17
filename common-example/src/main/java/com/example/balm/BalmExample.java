package com.example.balm;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.minecraft.world.item.Items;

public class BalmExample {
    public static void initialize(BalmRegistrars registrars) {
        Balm.config().registerConfig(ExampleConfig.class);

        registrars.compostables(registrar -> registrar.register(Items.DIAMOND, 1f));

        System.out.println("Hello common");
    }
}
