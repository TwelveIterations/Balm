package net.blay09.mods.balm.api.config;

@Deprecated(since = "1.21.5")
public interface BalmConfigProperty<T> {
    Class<T> getType();

    Class<T> getInnerType();

    T getValue();

    void setValue(T value);

    T getDefaultValue();
}
