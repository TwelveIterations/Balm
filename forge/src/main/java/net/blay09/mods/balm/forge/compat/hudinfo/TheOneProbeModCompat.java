package net.blay09.mods.balm.forge.compat.hudinfo;

import mcjty.theoneprobe.api.*;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.api.compat.hudinfo.HudInfoOutput;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.InterModComms;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TheOneProbeModCompat {

    public static void register() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", BalmTheOneProbeInitializer::new);
    }

    public static class BalmTheOneProbeInitializer implements Function<ITheOneProbe, Void> {
        @Nullable
        @Override
        public Void apply(@Nullable ITheOneProbe top) {
            if (top != null) {
                top.registerProvider(new BalmProbeInfoProvider());
            }
            return null;
        }
    }

    private static class BalmProbeInfoProvider implements IProbeInfoProvider {

        private static final ResourceLocation ID = new ResourceLocation("balm", "top");

        @Override
        public ResourceLocation getID() {
            return ID;
        }

        @Override
        public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData hitData) {
            final var modSupport = ((CommonBalmModSupportHudInfo) Balm.getModSupport().hudInfo());
            final var blockInfoProviders = modSupport.getBlockInfoProviders(state.getBlock());
            if (blockInfoProviders.isEmpty()) {
                return;
            }

            final var output = new TheOneProbeHudInfoInfoOutput(info);
            final var context = new BlockInfoContext(
                    level,
                    hitData.getPos(),
                    state,
                    level.getBlockEntity(hitData.getPos()),
                    new BlockHitResult(hitData.getHitVec(), hitData.getSideHit(), hitData.getPos(), false),
                    player);
            for (final var blockInfoProvider : blockInfoProviders) {
                blockInfoProvider.apply(context, output);
            }
        }
    }

    private record TheOneProbeHudInfoInfoOutput(IProbeInfo info) implements HudInfoOutput {

        @Override
        public void text(Component component) {
            info.text(component);
        }

        @Override
        public void progress(float progress) {
            info.progress((int) (progress * 100f), 100);
        }

        @Override
        public void progress(int progress, int maxProgress) {
            info.progress(progress, maxProgress);
        }
    }
}
