package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.event.EventMapper;

public interface ConfigCallback {

    @FunctionalInterface
    interface Loaded {
        void handle(BalmConfigSchema schema);

        EventMapper<Loaded> EVENT = EventMapper.createUnbound("ConfigCallback.Loaded");
    }

    @FunctionalInterface
    interface Reloaded {
        void handle(BalmConfigSchema schema);

        EventMapper<Reloaded> EVENT = EventMapper.createUnbound("ConfigCallback.Reloaded");
    }

}
