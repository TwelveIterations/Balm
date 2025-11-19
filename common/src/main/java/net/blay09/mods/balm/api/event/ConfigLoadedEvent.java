package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ConfigCallback#LOADED} instead.
 */
@Deprecated
public class ConfigLoadedEvent extends BalmEvent {

    private final BalmConfigSchema schema;

    public ConfigLoadedEvent(BalmConfigSchema schema) {
        this.schema = schema;
    }

    public BalmConfigSchema getSchema() {
        return schema;
    }

}
