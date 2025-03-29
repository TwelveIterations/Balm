package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.common.command.BalmCommand;
import net.minecraft.resources.ResourceLocation;

public class BaseModule implements BalmModule {
    private static final String MOD_ID = "balm";

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(MOD_ID, "common");
    }

    @Override
    public void registerCommands(BalmCommands commands) {
        commands.register(BalmCommand::register);
    }

    @Override
    public void registerNetworking(BalmNetworking networking) {
        networking.allowClientAndServerOnly("balm");
        networking.defineNetworkVersion(MOD_ID, "2");
    }

    @Override
    public void registerCapabilities(BalmCapabilities capabilities) {
        CommonCapabilities.initialize(capabilities);
    }
}
