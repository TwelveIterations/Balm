package net.blay09.mods.balm.platform.compatibility.config.internal;

import com.mrcrayfish.configured.api.*;
import com.mrcrayfish.configured.api.util.ConfigScreenHelper;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenFactory;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlContextImpl;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlRegistry;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.schema.*;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ClassUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ConfiguredConfigProvider implements IModConfigProvider {
    private static final Logger logger = LoggerFactory.getLogger(ConfiguredConfigProvider.class);

    @Nullable
    private static IModConfig mapConfig(BalmConfigSchema schema, @Nullable MutableLoadedConfig config) {
        if(config == null) {
            return null;
        }

        return new IModConfig() {
            @Override
            public ActionResult update(IConfigEntry entry) {
                Balm.config().saveLocalConfig(schema, config);
                return ActionResult.success();
            }

            @Override
            public IConfigEntry createRootEntry() {
                return mapConfigSchema(schema, config);
            }

            @Override
            public ConfigType getType() {
                return ConfigType.UNIVERSAL;
            }

            @Override
            public String getFileName() {
                return Balm.config().getConfigFile(schema).getName();
            }

            @Override
            public String getModId() {
                return schema.identifier().getNamespace();
            }
        };
    }

    private static IConfigEntry mapConfigSchema(BalmConfigSchema schema, MutableLoadedConfig config) {
        final var children = new ArrayList<IConfigEntry>();
        for (final var rootProperty : schema.rootProperties()) {
            children.add(mapConfigProperty(config, rootProperty));
        }
        for (final var category : schema.categories()) {
            children.add(mapConfigCategory(config, category));
        }
        return new IConfigEntry() {
            @Override
            public List<IConfigEntry> getChildren() {
                return children;
            }

            @Override
            public boolean isRoot() {
                return true;
            }

            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public @Nullable IConfigValue<?> getValue() {
                return null;
            }

            @Override
            public String getEntryName() {
                return "";
            }

            @Override
            public @Nullable Component getTooltip() {
                return null;
            }

            @Override
            public String getTranslationKey() {
                return ConfigLocalization.forTitle(schema);
            }
        };
    }

    private static IConfigEntry mapConfigCategory(MutableLoadedConfig config, ConfigCategory category) {
        final var children = category.properties().stream()
                .map(property -> mapConfigProperty(config, property)).toList();
        return new IConfigEntry() {
            @Override
            public List<IConfigEntry> getChildren() {
                return children;
            }

            @Override
            public boolean isRoot() {
                return false;
            }

            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public @Nullable IConfigValue<?> getValue() {
                return null;
            }

            @Override
            public String getEntryName() {
                return category.name();
            }

            @Override
            public @Nullable Component getTooltip() {
                return null;
            }

            @Override
            public String getTranslationKey() {
                return ConfigLocalization.forCategory(category);
            }
        };
    }

    private static <T> IConfigEntry mapConfigProperty(MutableLoadedConfig config, ConfiguredProperty<T> property) {
        final var customEntry = mapCustomControlConfigProperty(config, property);
        if (customEntry != null) {
            return customEntry;
        }

        final var initialValue = config.getRaw(property);
        return new IConfigEntry() {
            @Override
            public List<IConfigEntry> getChildren() {
                return List.of();
            }

            @Override
            public boolean isRoot() {
                return false;
            }

            @Override
            public boolean isLeaf() {
                return true;
            }

            @Override
            public IConfigValue<?> getValue() {
                return new IConfigValue<T>() {
                    @Override
                    public T get() {
                        return config.getRaw(property);
                    }

                    @Override
                    public T getDefault() {
                        return property.defaultValue();
                    }

                    @Override
                    public void set(T o) {
                        config.setRaw(property, property.validateValue(o).getOrThrow());
                    }

                    @Override
                    public boolean isValid(T o) {
                        if (!ClassUtils.isAssignable(o.getClass(), property.type(), true)) {
                            return false;
                        }
                        return property.validateValue(o).isSuccess();
                    }

                    @Override
                    public boolean isDefault() {
                        return Objects.equals(property.defaultValue(), config.getRaw(property));
                    }

                    @Override
                    public boolean isChanged() {
                        return !Objects.equals(config.getRaw(property), initialValue);
                    }

                    @Override
                    public void restore() {
                        config.setRaw(property, property.defaultValue());
                    }

                    @Override
                    public Component getComment() {
                        return Component.translatable(ConfigLocalization.forPropertyTooltip(property));
                    }

                    @Override
                    public String getTranslationKey() {
                        return ConfigLocalization.forProperty(property);
                    }

                    @Override
                    public @Nullable Component getValidationHint() {
                        return ConfiguredConfigProvider.getValidationHint(property);
                    }

                    @Override
                    public String getName() {
                        return property.name();
                    }

                    @Override
                    public void cleanCache() {
                    }

                    @Override
                    public boolean requiresWorldRestart() {
                        return false;
                    }

                    @Override
                    public boolean requiresGameRestart() {
                        return false;
                    }
                };
            }

            @Override
            public String getEntryName() {
                return property.category() + "." + property.name();
            }

            @Override
            public Component getTooltip() {
                return Component.translatable(ConfigLocalization.forPropertyTooltip(property));
            }

            @Override
            public String getTranslationKey() {
                return ConfigLocalization.forPropertyTooltip(property);
            }
        };
    }

    @Nullable
    private static <T> IConfigEntry mapCustomControlConfigProperty(MutableLoadedConfig config, ConfiguredProperty<T> property) {
        final var customControlId = property.customControl().orElse(null);
        if (customControlId == null) {
            return null;
        }

        final var binding = new ConfigControlBinding<>(property, config);
        final var context = new ConfigControlContextImpl(46, 20, _ -> {
            throw new IllegalArgumentException("No configuration bindings are available in this context");
        });
        final var entry = ConfigControlRegistry.createElement(customControlId, binding, context).orElse(null);
        switch (entry) {
            case IConfigEntry configEntry -> {
                return configEntry;
            }
            case IConfigValue<?> configValue -> {
                return new ValueEntry(configValue);
            }
            case null -> {
                return null;
            }
            default -> {
                logger.warn("Configured control for {}/{}.{} must return IConfigEntry or IConfigValue for Configured, got {}. Falling back to default control.",
                        property.parentSchema().identifier(), property.category(), property.name(), entry.getClass().getName());
                return null;
            }
        }
    }

    private static @Nullable Component getValidationHint(ConfiguredProperty<?> property) {
        return switch (property) {
            case ConfiguredInt configuredInt when (configuredInt.minValue().isPresent() || configuredInt.maxValue().isPresent()) ->
                    getRangeValidationHint(configuredInt.minValue(), configuredInt.maxValue());
            case ConfiguredLong configuredLong when (configuredLong.minValue().isPresent() || configuredLong.maxValue().isPresent()) ->
                    getRangeValidationHint(configuredLong.minValue(), configuredLong.maxValue());
            case ConfiguredFloat configuredFloat when (configuredFloat.minValue().isPresent() || configuredFloat.maxValue().isPresent()) ->
                    getRangeValidationHint(configuredFloat.minValue(), configuredFloat.maxValue());
            case ConfiguredDouble configuredDouble when (configuredDouble.minValue().isPresent() || configuredDouble.maxValue().isPresent()) ->
                    getRangeValidationHint(configuredDouble.minValue(), configuredDouble.maxValue());
            default -> null;
        };
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Component getRangeValidationHint(Optional<?> minValue, Optional<?> maxValue) {
        return Component.translatable("gui.balm.configuration.validation.range",
                minValue.map(it -> Component.literal(String.valueOf(it))).orElseGet(() -> Component.translatable("gui.balm.configuration.validation.negative_infinity")),
                maxValue.map(it -> Component.literal(String.valueOf(it))).orElseGet(() -> Component.translatable("gui.balm.configuration.validation.positive_infinity")));
    }

    public static BalmConfigScreenFactory getConfigScreenFactory(String modId) {
        return parentScreen -> createConfigScreen(modId, parentScreen);
    }

    public static Screen createConfigScreen(String modId, Screen parent) {
        final var configs = Balm.config().getSchemasByNamespace(modId);
        final var configsByType = new HashMap<ConfigType, Set<IModConfig>>();
        final Set<IModConfig> universalConfigs = configs.stream()
                .filter(it -> !it.identifier().getPath().equals("client"))
                .map(schema -> mapConfig(schema, Balm.config().getLocalConfig(schema)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        final Set<IModConfig> clientConfigs = configs.stream()
                .filter(it -> it.identifier().getPath().equals("client"))
                .map(schema -> mapConfig(schema, Balm.config().getLocalConfig(schema)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        configsByType.put(ConfigType.UNIVERSAL, universalConfigs);
        configsByType.put(ConfigType.CLIENT, clientConfigs);
        return ConfigScreenHelper.createSelectionScreen(parent,
                Component.translatable(ConfigLocalization.forTitle(modId)),
                configsByType
        );
    }

    @Override
    public Set<IModConfig> getConfigurationsForMod(ModContext modContext) {
        final var configs = Balm.config().getSchemasByNamespace(modContext.modId());
        return configs.stream()
                .map(schema -> mapConfig(schema, Balm.config().getLocalConfig(schema)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
