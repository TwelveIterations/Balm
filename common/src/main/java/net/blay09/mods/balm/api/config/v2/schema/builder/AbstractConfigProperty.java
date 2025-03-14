package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.impl.ConfigSchemaImpl;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public abstract class AbstractConfigProperty<T> implements ConfiguredProperty<T> {
    private final ConfigSchemaImpl schema;
    private final String category;
    private final String name;
    private final String comment;

    public AbstractConfigProperty(ConfigPropertyBuilder parent) {
        schema = parent.schema;
        category = parent.category;
        name = parent.name;
        comment = parent.comment;
    }

    @Override
    public ConfigSchemaImpl parentSchema() {
        return schema;
    }

    @Override
    public String category() {
        return category;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String comment() {
        return comment;
    }
}
