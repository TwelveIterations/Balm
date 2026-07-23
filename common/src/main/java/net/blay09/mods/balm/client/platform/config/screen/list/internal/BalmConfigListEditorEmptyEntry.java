package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorEntry;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class BalmConfigListEditorEmptyEntry<T> extends BalmConfigListEditorEntry<T> {
    private static final Component LABEL = Component.translatable("gui.balm.configuration.list.empty");

    public BalmConfigListEditorEmptyEntry(BalmConfigListEditorScreen<T> screen) {
        super(screen, new BalmConfigListEditorValue<>(null));
    }

    @Override
    protected void extractEntryContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
        graphics.text(context.font(), LABEL, getContentLeftAfterDragHandle(), getContentY() + 5, 0xFFFFFFFF);
    }
}
