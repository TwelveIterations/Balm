package net.blay09.mods.balm.common.compat.hudinfo;

import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.ProgressArrowComponent;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.api.compat.hudinfo.HudInfoOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

public class WTHITModCompat implements IWailaClientPlugin {

    @Override
    public void register(IClientRegistrar registrar) {
        registrar.body(new BalmBlockComponentProvider(), Block.class);
    }

    private static class BalmBlockComponentProvider implements IBlockComponentProvider {
        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            final var block = accessor.getBlock();
            final var modSupport = ((CommonBalmModSupportHudInfo) Balm.modSupport().hudInfo());
            final var blockInfoProviders = modSupport.getBlockInfoProviders(block);
            if (blockInfoProviders.isEmpty()) {
                return;
            }

            final var output = new WTHITHudInfoOutput(tooltip);
            final var context = new BlockInfoContext(
                    accessor.getWorld(),
                    accessor.getPosition(),
                    accessor.getBlockState(),
                    accessor.getBlockEntity(),
                    accessor.getBlockHitResult(),
                    accessor.getPlayer());
            for (final var blockInfoProvider : blockInfoProviders) {
                blockInfoProvider.apply(context, output);
            }
        }

    }

    private record WTHITHudInfoOutput(ITooltip tooltip) implements HudInfoOutput {
        @Override
        public void text(Component component) {
            tooltip.addLine(component);
        }

        @Override
        public void progress(float progress) {
            tooltip.addLine(new ProgressArrowComponent(progress));
        }
    }
}
