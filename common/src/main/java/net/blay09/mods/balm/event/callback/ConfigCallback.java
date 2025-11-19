package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.event.EventMapper;

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
