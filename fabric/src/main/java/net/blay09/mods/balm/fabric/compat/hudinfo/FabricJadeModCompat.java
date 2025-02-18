package net.blay09.mods.balm.fabric.compat.hudinfo;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.api.compat.hudinfo.HudInfoOutput;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.impl.ui.ProgressElement;
import snownee.jade.impl.ui.SimpleProgressStyle;

@WailaPlugin("balm-fabric")
public class FabricJadeModCompat implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new BalmBlockComponentProvider(), Block.class);
    }

    private static class BalmBlockComponentProvider implements IBlockComponentProvider {

        private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("balm", "jade");

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            final var block = accessor.getBlock();
            final var modSupport = ((CommonBalmModSupportHudInfo) Balm.getModSupport().hudInfo());
            final var blockInfoProviders = modSupport.getBlockInfoProviders(block);
            if (blockInfoProviders.isEmpty()) {
                return;
            }

            final var output = new JadeHudInfoOutput(tooltip);
            final var context = new BlockInfoContext(
                    accessor.getLevel(),
                    accessor.getPosition(),
                    accessor.getBlockState(),
                    accessor.getBlockEntity(),
                    accessor.getHitResult(),
                    accessor.getPlayer());
            for (final var blockInfoProvider : blockInfoProviders) {
                blockInfoProvider.apply(context, output);
            }
        }

        @Override
        public ResourceLocation getUid() {
            return ID;
        }
    }

    private record JadeHudInfoOutput(ITooltip tooltip) implements HudInfoOutput {
        @Override
        public void text(Component component) {
            tooltip.add(component);
        }

        @Override
        public void progress(float progress) {
            tooltip.add(new ProgressElement(progress, Component.empty(), new SimpleProgressStyle(), BoxStyle.getNestedBox(), false));
        }
    }
}
