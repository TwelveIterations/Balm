package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BalmConfigScreenState {
    private final Map<BalmConfigSchema, MutableLoadedConfig> configs = new HashMap<>();
    private final Map<ConfiguredProperty<?>, Component> validationErrors = new HashMap<>();
    private final Runnable onValidationChanged;
    private final Runnable onValueChanged;

    public BalmConfigScreenState(Runnable onValidationChanged, Runnable onValueChanged) {
        this.onValidationChanged = onValidationChanged;
        this.onValueChanged = onValueChanged;
    }

    public BalmConfigScreenState(BalmConfigScreenState parent, Runnable onValidationChanged, Runnable onValueChanged) {
        this.onValidationChanged = onValidationChanged;
        this.onValueChanged = onValueChanged;
        parent.configs.forEach((schema, config) -> configs.put(schema, config.copy()));
        validationErrors.putAll(parent.validationErrors);
    }

    public <T> MutableLoadedConfig configFor(ConfiguredProperty<T> property) {
        return configs.computeIfAbsent(property.parentSchema(), schema -> {
            final var localConfig = Balm.config().getLocalConfig(schema);
            return localConfig != null ? localConfig.copy() : schema.defaults().mutable(schema);
        });
    }

    public <T> ConfigControlBinding<T> bindingFor(ConfiguredProperty<T> property) {
        return new ConfigControlBinding<>(property,
                () -> property.getRaw(configFor(property)),
                value -> trySetValue(property, value));
    }

    public <T> void trySetValue(ConfiguredProperty<T> property, T value) {
        final var result = property.validateValue(value);
        result.error().ifPresentOrElse(error -> validationErrors.put(property, Component.literal(error.message())), () -> {
            final var config = configFor(property);
            final var oldValue = property.getRaw(config);
            final var validatedValue = result.getOrThrow();
            validationErrors.remove(property);
            property.setRaw(config, validatedValue);
            if (!Objects.equals(oldValue, validatedValue)) {
                onValueChanged.run();
            }
        });
        onValidationChanged.run();
    }

    public void setValidationError(ConfiguredProperty<?> property, Component error) {
        validationErrors.put(property, error);
        onValidationChanged.run();
    }

    public void clearValidationError(ConfiguredProperty<?> property) {
        if (validationErrors.remove(property) != null) {
            onValidationChanged.run();
        }
    }

    public void applyTo(BalmConfigScreenState parent) {
        parent.configs.clear();
        configs.forEach((schema, config) -> parent.configs.put(schema, config.copy()));
        parent.validationErrors.clear();
        parent.validationErrors.putAll(validationErrors);
        parent.onValueChanged.run();
        parent.onValidationChanged.run();
    }

    @Nullable
    public Component getValidationError(ConfiguredProperty<?> property) {
        return validationErrors.get(property);
    }

    public boolean hasValidationErrors() {
        return !validationErrors.isEmpty();
    }

    public void save() {
        for (final var entry : configs.entrySet()) {
            Balm.config().saveLocalConfig(entry.getKey(), entry.getValue());
        }
    }
}
