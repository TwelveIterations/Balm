package com.example.balm.client;

import net.blay09.mods.balm.client.BalmClientRegistrars;

public class BalmExampleClient {
    public static void initialize(BalmClientRegistrars registrars) {
        System.out.println("Hello client");
        registrars.registerModule(new ClientCommandTestModule());
    }
}
