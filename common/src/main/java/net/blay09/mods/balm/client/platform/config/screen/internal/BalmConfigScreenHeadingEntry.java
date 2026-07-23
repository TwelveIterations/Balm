package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenEntry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BalmConfigScreenHeadingEntry extends BalmConfigScreenEntry {
    private final Component title;
    private boolean hovered;
    private final NarratableEntry narration = new NarratableEntry() {
        @Override
        public NarrationPriority narrationPriority() {
            return hovered ? NarrationPriority.HOVERED : NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(NarrationElementOutput output) {
            output.add(NarratedElementType.TITLE, title);
        }
    };

    public BalmConfigScreenHeadingEntry(BalmConfigScreenContext context, Component title) {
        super(context);
        this.title = title;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return List.of(narration);
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
        this.hovered = hovered;
        graphics.centeredText(context.font(), title, getContentXMiddle(), getContentY() + 5, 0xFFFFFFFF);
    }
}
