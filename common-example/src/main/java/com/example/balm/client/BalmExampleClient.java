package com.example.balm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.platform.event.callback.RenderCallback;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.Kuma;

public class BalmExampleClient {
    public static void initialize(BalmClientRegistrars registrars) {
        System.out.println("Hello client");
        registrars.registerModule(new ClientCommandTestModule());
        registrars.registerModule(new ConfigScreenTestModule());

        RenderCallback.UpdateFov.EVENT.register((entity, fov) -> Kuma.isDown(InputBinding.key(InputConstants.KEY_F)) ? (float) Math.sin(entity.tickCount) : fov);
        RenderCallback.UpdateFov.EVENT.register((entity, fov) -> Kuma.isDown(InputBinding.key(InputConstants.KEY_G)) ? 2f : fov);
    }
}
