package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.resources.Identifier;

public interface ConfigCallback {

    @FunctionalInterface
    interface Loaded {
        void handle(BalmConfigSchema schema);

        EventMapper<Loaded> EVENT = EventMapper.createUnbound("ConfigCallback.Loaded");

        static EventMapper<Loaded> forSchema(Identifier identifier) {
            return ConfigCallback.Loaded.EVENT.filter(identifier.toString(), (base) -> schema -> {
                if (schema.identifier().equals(identifier)) {
                    base.handle(schema);
                }
            });
        }
    }

    @FunctionalInterface
    interface Reloaded {
        void handle(BalmConfigSchema schema);

        EventMapper<Reloaded> EVENT = EventMapper.createUnbound("ConfigCallback.Reloaded");

        static EventMapper<Reloaded> forSchema(Identifier identifier) {
            return ConfigCallback.Reloaded.EVENT.filter(identifier.toString(), (base) -> schema -> {
                if (schema.identifier().equals(identifier)) {
                    base.handle(schema);
                }
            });
        }
    }

}
