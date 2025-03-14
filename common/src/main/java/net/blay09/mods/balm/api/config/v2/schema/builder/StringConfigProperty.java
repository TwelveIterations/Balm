package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredString;

public class StringConfigProperty extends AbstractConfigProperty<String> implements ConfiguredString {
    private final String defaultValue;

    public StringConfigProperty(ConfigPropertyBuilder parent, String defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<String> type() {
        return String.class;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }
}
