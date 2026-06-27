package net.blay09.mods.balm.platform.config.schema.builder;

import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.internal.ConfigSchemaImpl;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractConfigProperty<T> implements ConfiguredProperty<T> {
    private final ConfigSchemaImpl schema;
    private final String category;
    private final String name;
    private final String comment;
    private final boolean synced;
    private final @Nullable Identifier customControl;

    public AbstractConfigProperty(ConfigPropertyBuilder parent) {
        schema = parent.schema;
        category = parent.category;
        name = parent.name;
        comment = parent.comment;
        synced = parent.synced;
        customControl = parent.customControl;
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

    @Override
    public boolean synced() {
        return synced;
    }

    @Override
    public Optional<Identifier> customControl() {
        return Optional.ofNullable(customControl);
    }
}
