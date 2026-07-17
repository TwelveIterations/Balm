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

public class BalmConfigScreenState {
    private final Map<BalmConfigSchema, MutableLoadedConfig> configs = new HashMap<>();
    private final Map<ConfiguredProperty<?>, Component> validationErrors = new HashMap<>();
    private final Runnable onValidationChanged;

    public BalmConfigScreenState(Runnable onValidationChanged) {
        this.onValidationChanged = onValidationChanged;
    }

    public <T> MutableLoadedConfig configFor(ConfiguredProperty<T> property) {
        return configs.computeIfAbsent(property.parentSchema(), schema -> {
            final var localConfig = Balm.config().getLocalConfig(schema);
            return localConfig != null ? localConfig.copy() : schema.defaults().mutable(schema);
        });
    }

    public <T> ConfigControlBinding<T> bindingFor(ConfiguredProperty<T> property) {
        return new ConfigControlBinding<>(property, () -> property.getRaw(configFor(property)), value -> trySetValue(property, value));
    }

    public <T> void trySetValue(ConfiguredProperty<T> property, T value) {
        final var result = property.validateValue(value);
        result.error().ifPresentOrElse(error -> validationErrors.put(property, Component.literal(error.message())), () -> {
            validationErrors.remove(property);
            property.setRaw(configFor(property), result.getOrThrow());
        });
        onValidationChanged.run();
    }

    public void setValidationError(ConfiguredProperty<?> property, Component error) {
        validationErrors.put(property, error);
        onValidationChanged.run();
    }

    public void clearValidationError(ConfiguredProperty<?> property) {
        validationErrors.remove(property);
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
