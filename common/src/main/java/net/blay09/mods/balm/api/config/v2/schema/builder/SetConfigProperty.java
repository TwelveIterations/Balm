package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredSet;
import net.blay09.mods.balm.api.config.v2.schema.NestedTypeHolder;

import java.util.Set;

public class SetConfigProperty<T> extends AbstractConfigProperty<Set<T>> implements ConfiguredSet<T>, NestedTypeHolder<T> {
    private final Class<T> nestedType;
    private final Set<T> defaultValue;

    public SetConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, Set<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<?> type() {
        return Set.class;
    }

    @Override
    public Class<T> nestedType() {
        return nestedType;
    }

    @Override
    public Set<T> defaultValue() {
        return defaultValue;
    }
}
