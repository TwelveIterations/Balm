package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredEnum;

public class EnumConfigProperty<T extends Enum<T>> extends AbstractConfigProperty<T> implements ConfiguredEnum<T> {
    private final T defaultValue;

    public EnumConfigProperty(ConfigPropertyBuilder parent, T defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<T> type() {
        return defaultValue.getDeclaringClass();
    }

    @Override
    public T defaultValue() {
        return defaultValue;
    }
}
