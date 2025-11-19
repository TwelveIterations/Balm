package net.blay09.mods.balm.module.internal;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.commands.internal.InternalsCommand;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.blay09.mods.balm.common.resources.ConfigResourceCondition;
import net.minecraft.resources.Identifier;

/**
 * Internal module that registers Balm's own capabilities, commands, resources, and networking.
 * Use {@link BalmModule} for your own modules.
 * @see BalmModule
 * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)
 */
public final class InternalsModule implements BalmModule {
    private static final String MOD_ID = "balm";

    @Override
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath(MOD_ID, "common");
    }

    @Override
    public void registerCommands(BalmCommands commands) {
        commands.register(InternalsCommand::register);
    }

    @Override
    public void registerNetworking(BalmNetworking networking) {
        networking.allowClientAndServerOnly("balm");
        networking.defineNetworkVersion(MOD_ID, "4");
    }

    @Override
    public void registerResources(BalmResources resources) {
        resources.registerResourceCondition(Identifier.fromNamespaceAndPath(MOD_ID, "config"), ConfigResourceCondition.CODEC);
    }

    @Override
    public void registerCapabilities(BalmCapabilities capabilities) {
        CommonCapabilities.initialize(capabilities);
    }
}
