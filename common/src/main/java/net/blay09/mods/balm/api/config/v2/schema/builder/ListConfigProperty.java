package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredList;
import net.blay09.mods.balm.api.config.v2.schema.NestedTypeHolder;

import java.util.List;

public class ListConfigProperty<T> extends AbstractConfigProperty<List<T>> implements ConfiguredList<T>, NestedTypeHolder<T> {
    private final Class<T> nestedType;
    private final List<T> defaultValue;

    public ListConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, List<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<?> type() {
        return List.class;
    }

    @Override
    public Class<T> nestedType() {
        return nestedType;
    }

    @Override
    public List<T> defaultValue() {
        return defaultValue;
    }
}
