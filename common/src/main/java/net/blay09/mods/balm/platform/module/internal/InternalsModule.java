package net.blay09.mods.balm.platform.module.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.commands.BalmCommands;
import net.blay09.mods.balm.commands.internal.InternalsCommand;
import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.balm.network.internal.RemotePlayerModList;
import net.blay09.mods.balm.network.protocol.common.custom.internal.ServerboundModListMessage;
import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.capabilities.internal.CommonCapabilitiesImpl;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.internal.ConfigResourceCondition;
import net.minecraft.resources.Identifier;

/**
 * Internal module that registers Balm's own capabilities, commands, resources, and networking.
 * Use {@link BalmModule} for your own modules.
 *
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
        networking.registerServerboundPacket(ServerboundModListMessage.TYPE, ServerboundModListMessage.class, ServerboundModListMessage.STREAM_CODEC, (player, message) -> RemotePlayerModList.store(player, message.modList()));
    }

    @Override
    public void registerResourceConditions(BalmResourceConditionRegistrar resourceConditions) {
        resourceConditions.register("config", ConfigResourceCondition.CODEC);
    }

    @Override
    public void registerCapabilities(BalmCapabilities capabilities) {
        CommonCapabilitiesImpl.registerCapabilities(capabilities);
    }

    @Override
    public void initialize() {
        RemotePlayerModList.initialize();
    }
}
