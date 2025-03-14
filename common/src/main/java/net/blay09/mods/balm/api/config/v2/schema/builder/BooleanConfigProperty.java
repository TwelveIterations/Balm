package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredBoolean;

public class BooleanConfigProperty extends AbstractConfigProperty<Boolean> implements ConfiguredBoolean {
    private final boolean defaultValue;

    public BooleanConfigProperty(ConfigPropertyBuilder parent, boolean defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Boolean> type() {
        return Boolean.class;
    }

    @Override
    public Boolean defaultValue() {
        return defaultValue;
    }
}
