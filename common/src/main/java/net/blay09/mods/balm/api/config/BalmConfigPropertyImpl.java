package net.blay09.mods.balm.api.config;

import java.lang.reflect.Field;

@Deprecated(forRemoval = true, since = "1.21.5")
public class BalmConfigPropertyImpl<T> implements BalmConfigProperty<T> {
    private final BalmConfigData configData;
    private final Field categoryField;
    private final Field propertyField;
    private final BalmConfigData defaultConfig;

    public BalmConfigPropertyImpl(BalmConfigData configData, Field categoryField, Field propertyField, BalmConfigData defaultConfig) {
        this.configData = configData;
        this.categoryField = categoryField;
        this.propertyField = propertyField;
        this.defaultConfig = defaultConfig;
    }

    @Override
    public Class<T> getType() {
        return (Class<T>) propertyField.getType();
    }

    @Override
    public Class<T> getInnerType() {
        var expectedTypeAnnotation = propertyField.getAnnotation(ExpectedType.class);
        if (expectedTypeAnnotation != null) {
            return (Class<T>) expectedTypeAnnotation.value();
        }
        return null;
    }

    @Override
    public T getValue() {
        try {
            var instance = categoryField != null ? categoryField.get(configData) : configData;
            return (T) propertyField.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return getDefaultValue();
    }

    @Override
    public void setValue(T value) {
        try {
            var instance = categoryField != null ? categoryField.get(configData) : configData;
            propertyField.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getDefaultValue() {
        try {
            var instance = categoryField != null ? categoryField.get(defaultConfig) : defaultConfig;
            return (T) propertyField.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
