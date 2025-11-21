package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

public interface CreativeModeTabCallback {

    @FunctionalInterface
    interface BuildContents {
        void handle(CreativeModeTab tab, CreativeModeTab.Output output);

        EventMapper<BuildContents> EVENT = EventMapper.createUnbound("CreativeModeTabCallback.BuildContents");

        static EventMapper<BuildContents> forTab(Identifier identifier) {
            return BuildContents.EVENT.filter(identifier.toString(), (base) -> (tab, output) -> {
                final var tabId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
                if (identifier.equals(tabId)) {
                    base.handle(tab, output);
                }
            });
        }
    }

}
