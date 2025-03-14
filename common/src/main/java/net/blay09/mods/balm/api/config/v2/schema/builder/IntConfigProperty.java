package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredInt;

public class IntConfigProperty extends AbstractConfigProperty<Integer> implements ConfiguredInt {
    private final int defaultValue;

    public IntConfigProperty(ConfigPropertyBuilder parent, int defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Integer> type() {
        return Integer.class;
    }

    @Override
    public Integer defaultValue() {
        return defaultValue;
    }
}
