package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.item.CreativeModeTab;

@FunctionalInterface
public interface CreativeModeTabCallback {
    void handle(CreativeModeTab tab, CreativeModeTab.Output output);

    EventMapper<CreativeModeTabCallback> BUILD_CONTENTS = EventMapper.createUnbound();
}
