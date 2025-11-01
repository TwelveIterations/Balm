package net.blay09.mods.balm.world.item;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.world.item.CreativeModeTab;

public class BuildCreativeModeTabContentsEvent extends BalmEvent {
    private final CreativeModeTab tab;
    private final CreativeModeTab.Output output;

    public BuildCreativeModeTabContentsEvent(CreativeModeTab tab, CreativeModeTab.Output output) {
        this.tab = tab;
        this.output = output;
    }

    public CreativeModeTab getTab() {
        return tab;
    }

    public CreativeModeTab.Output getOutput() {
        return output;
    }
}
