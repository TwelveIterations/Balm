package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.item.CreativeModeTab;

public interface CreativeModeTabCallback {

    @FunctionalInterface
    interface BuildContents {
        void handle(CreativeModeTab tab, CreativeModeTab.Output output);

        EventMapper<BuildContents> EVENT = EventMapper.createUnbound("CreativeModeTabCallback.BuildContents");
    }

}
