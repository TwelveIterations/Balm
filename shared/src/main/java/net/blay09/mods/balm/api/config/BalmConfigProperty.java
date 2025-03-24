package net.blay09.mods.balm.api.config;

public interface BalmConfigProperty<T> {
    Class<T> getType();

    Class<T> getInnerType();

    T getValue();

    void setValue(T value);

    T getDefaultValue();
}
