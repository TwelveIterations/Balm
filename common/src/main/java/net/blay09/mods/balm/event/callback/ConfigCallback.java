package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ConfigCallback {
    void handle(BalmConfigSchema schema);

    EventMapper<ConfigCallback> LOADED = EventMapper.createUnbound("ConfigCallback.LOADED");
    EventMapper<ConfigCallback> RELOADED = EventMapper.createUnbound("ConfigCallback.RELOADED");
}
