package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredFloat;

public class FloatConfigProperty extends AbstractConfigProperty<Float> implements ConfiguredFloat {
    private final float defaultValue;

    public FloatConfigProperty(ConfigPropertyBuilder parent, float defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Float> type() {
        return Float.class;
    }

    @Override
    public Float defaultValue() {
        return defaultValue;
    }
}
