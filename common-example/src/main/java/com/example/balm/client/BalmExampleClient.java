package com.example.balm.client;

import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.platform.config.ConfigControl;
import net.blay09.mods.balm.platform.config.schema.ConfigControlContext;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BalmExampleClient {
    public static void initialize(BalmClientRegistrars registrars) {
        System.out.println("Hello client");
        registrars.customConfigControls(configControls -> configControls.register("fancy_button", ConfigControl.<Boolean>builder()
                .element(FancyConfigButton::new)
                .build()));
        registrars.registerModule(new ClientCommandTestModule());
    }

    private static class FancyConfigButton extends Button.Plain {
        private final ConfigControlContext<Boolean> context;

        public FancyConfigButton(ConfigControlContext<Boolean> context) {
            super(0, 0, 150, 20, Component.empty(), (_) -> context.set(!context.get()), DEFAULT_NARRATION);
            this.context = context;
        }

        @Override
        public Component getMessage() {
            return Component.literal("Custom: " + context.get());
        }
    }
}
