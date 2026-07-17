package net.blay09.mods.balm.client.platform.config.screen;

import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.List;

public abstract class BalmConfigScreenEntry extends ContainerObjectSelectionList.Entry<BalmConfigScreenEntry> {
    protected final BalmConfigScreenContext context;

    protected BalmConfigScreenEntry(BalmConfigScreenContext context) {
        this.context = context;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of();
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return List.of();
    }
}
