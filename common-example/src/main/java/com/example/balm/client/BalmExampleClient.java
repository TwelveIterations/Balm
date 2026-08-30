package com.example.balm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.platform.config.ConfigControl;
import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.blay09.mods.balm.client.platform.event.callback.RenderCallback;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.Kuma;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BalmExampleClient {
    public static void initialize(BalmClientRegistrars registrars) {
        System.out.println("Hello client");
        registrars.customConfigControls(configControls -> configControls.register("fancy_button", ConfigControl.<Boolean>builder()
                .element(FancyConfigButton::new)
                .build()));
        registrars.registerModule(new ClientCommandTestModule());

        RenderCallback.UpdateFov.EVENT.register((entity, fov) -> Kuma.isDown(InputBinding.key(InputConstants.KEY_F)) ? (float) Math.sin(entity.tickCount) : fov);
        RenderCallback.UpdateFov.EVENT.register((entity, fov) -> Kuma.isDown(InputBinding.key(InputConstants.KEY_G)) ? 2f : fov);
    }

    private static class FancyConfigButton extends Button.Plain {
        private final ConfigControlBinding<Boolean> binding;

        public FancyConfigButton(ConfigControlBinding<Boolean> binding, ConfigControlContext context) {
            super(0, 0, context.entryWidth(), context.entryHeight(), Component.empty(), (_) -> binding.set(!binding.get()), DEFAULT_NARRATION);
            this.binding = binding;
        }

        @Override
        public Component getMessage() {
            return Component.literal("Custom: " + binding.get());
        }
    }
}
