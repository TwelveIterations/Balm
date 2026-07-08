package net.blay09.mods.balm.platform.compatibility.config.internal;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenFactory;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlRegistry;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;
import net.blay09.mods.balm.platform.config.schema.*;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.*;
import java.util.stream.Collectors;

public class ClothConfigSupport {
    public ClothConfigSupport() {
        BalmConfigScreenProviders.register("cloth-config", ClothConfigSupport::getConfigScreenFactory);
    }

    public static BalmConfigScreenFactory getConfigScreenFactory(String modId) {
        return parentScreen -> {
            final var builder = ConfigBuilder.create()
                    .setParentScreen(parentScreen)
                    .setTitle(Component.translatable(ConfigLocalization.forTitle(modId)));
            final var schemas = Balm.config().getSchemasByNamespace(modId);
            builder.setSavingRunnable(() -> {
                for (final var schema : schemas) {
                    final var config = Balm.config().getLocalConfig(schema);
                    if (config == null) {
                        throw new RuntimeException("No local config loaded for schema " + schema.identifier());
                    }
                    Balm.config().saveLocalConfig(schema, config);
                }
            });
            for (final var schema : schemas) {
                final var config = Balm.config().getLocalConfig(schema);
                if (config == null) {
                    throw new RuntimeException("No local config loaded for schema " + schema.identifier());
                }
                final var categories = schema.categories();
                ConfigCategory rootCategory = null;
                for (final var rootProperty : schema.rootProperties()) {
                    if (rootCategory == null) {
                        rootCategory = builder.getOrCreateCategory(Component.translatable(ConfigLocalization.forRootCategory(schema)));
                    }
                    addPropertyToBuilder(config, rootProperty, rootCategory, builder);
                }
                for (final var category : categories) {
                    final var categoryInstance = builder.getOrCreateCategory(Component.translatable(ConfigLocalization.forCategory(category)));
                    for (final var property : category.properties()) {
                        addPropertyToBuilder(config, property, categoryInstance, builder);
                    }
                }
            }

            return builder.build();
        };
    }

    @SuppressWarnings("unchecked")
    private static void addPropertyToBuilder(MutableLoadedConfig config, ConfiguredProperty<?> property, ConfigCategory categoryInstance, ConfigBuilder builder) {
        var displayName = Component.translatable(ConfigLocalization.forProperty(property));
        var tooltip = Component.translatable(ConfigLocalization.forPropertyTooltip(property));
        if (addCustomControlPropertyToBuilder(config, property, categoryInstance, displayName, tooltip)) {
            return;
        }

        switch (property) {
            case ConfiguredString stringProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrField(displayName, stringProperty.get(config))
                            .setDefaultValue(stringProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> setValue(config, stringProperty, displayName, tooltip, value))
                            .build()
            );
            case ConfiguredInt intProperty -> {
                var fieldBuilder = builder.entryBuilder().startIntField(displayName, intProperty.get(config))
                        .setDefaultValue(intProperty.defaultValue())
                        .setTooltip(tooltip)
                        .setSaveConsumer(value -> setValue(config, intProperty, displayName, tooltip, value));
                fieldBuilder = intProperty.minValue().map(fieldBuilder::setMin).orElse(fieldBuilder);
                fieldBuilder = intProperty.maxValue().map(fieldBuilder::setMax).orElse(fieldBuilder);
                categoryInstance.addEntry(fieldBuilder.build());
            }
            case ConfiguredFloat floatProperty -> {
                var fieldBuilder = builder.entryBuilder().startFloatField(displayName, floatProperty.get(config))
                        .setDefaultValue(floatProperty.defaultValue())
                        .setTooltip(tooltip)
                        .setSaveConsumer(value -> setValue(config, floatProperty, displayName, tooltip, value));
                fieldBuilder = floatProperty.minValue().map(fieldBuilder::setMin).orElse(fieldBuilder);
                fieldBuilder = floatProperty.maxValue().map(fieldBuilder::setMax).orElse(fieldBuilder);
                categoryInstance.addEntry(fieldBuilder.build());
            }
            case ConfiguredBoolean booleanProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startBooleanToggle(displayName, booleanProperty.get(config))
                            .setDefaultValue(booleanProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> setValue(config, booleanProperty, displayName, tooltip, value))
                            .build()
            );
            case ConfiguredEnum<?> enumProperty ->
                    addEnumPropertyToBuilder(config, enumProperty, categoryInstance, builder, displayName, tooltip);
            case ConfiguredList<?> listProperty when listProperty.nestedType() == String.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startStrList(displayName, (List<String>) listProperty.get(config))
                                    .setDefaultValue((List<String>) listProperty.defaultValue())
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredList<String>) listProperty, displayName, tooltip, value))
                                    .build()
                    );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Integer.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startIntList(displayName, (List<Integer>) listProperty.get(config))
                                    .setDefaultValue((List<Integer>) listProperty.defaultValue())
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredList<Integer>) listProperty, displayName, tooltip, value))
                                    .build()
                    );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Long.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startLongList(displayName, (List<Long>) listProperty.get(config))
                                    .setDefaultValue((List<Long>) listProperty.defaultValue())
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredList<Long>) listProperty, displayName, tooltip, value))
                                    .build()
                    );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Float.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startFloatList(displayName, (List<Float>) listProperty.get(config))
                                    .setDefaultValue((List<Float>) listProperty.defaultValue())
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredList<Float>) listProperty, displayName, tooltip, value))
                                    .build()
                    );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Double.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startDoubleList(displayName, (List<Double>) listProperty.get(config))
                                    .setDefaultValue((List<Double>) listProperty.defaultValue())
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredList<Double>) listProperty, displayName, tooltip, value))
                                    .build()
                    );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Identifier.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startStrList(displayName, listProperty.get(config).stream().map(Objects::toString).toList())
                                    .setDefaultValue(listProperty.defaultValue().stream().map(Objects::toString).toList())
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredList<Identifier>) listProperty, displayName, tooltip, value.stream().map(Identifier::tryParse).filter(Objects::nonNull).collect(Collectors.toList())))
                                    .build()
                    );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == String.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startStrList(displayName, new ArrayList<>((Set<String>) setProperty.get(config)))
                                    .setDefaultValue(new ArrayList<>((Set<String>) setProperty.defaultValue()))
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredSet<String>) setProperty, displayName, tooltip, new HashSet<>(value)))
                                    .build()
                    );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Integer.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startIntList(displayName, new ArrayList<>((Set<Integer>) setProperty.get(config)))
                                    .setDefaultValue(new ArrayList<>((Set<Integer>) setProperty.defaultValue()))
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredSet<Integer>) setProperty, displayName, tooltip, new HashSet<>(value)))
                                    .build()
                    );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Long.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startLongList(displayName, new ArrayList<>((Set<Long>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Long>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> setValue(config, (ConfiguredSet<Long>) setProperty, displayName, tooltip, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Float.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startFloatList(displayName, new ArrayList<>((Set<Float>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Float>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> setValue(config, (ConfiguredSet<Float>) setProperty, displayName, tooltip, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Double.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startDoubleList(displayName, new ArrayList<>((Set<Double>) setProperty.get(config)))
                                    .setDefaultValue(new ArrayList<>((Set<Double>) setProperty.defaultValue()))
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredSet<Double>) setProperty, displayName, tooltip, new HashSet<>(value)))
                                    .build()
                    );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Identifier.class ->
                    categoryInstance.addEntry(
                            builder.entryBuilder().startStrList(displayName, setProperty.get(config).stream().map(Objects::toString).toList())
                                    .setDefaultValue(setProperty.defaultValue().stream().map(Objects::toString).toList())
                                    .setTooltip(tooltip)
                                    .setSaveConsumer(value -> setValue(config, (ConfiguredSet<Identifier>) setProperty, displayName, tooltip, value.stream().map(Identifier::tryParse).filter(Objects::nonNull).collect(Collectors.toSet())))
                                    .build()
                    );
            default -> {
            }
        }
    }

    private static <T> void setValue(MutableLoadedConfig config, ConfiguredProperty<T> property, Component displayName, Component tooltip, T value) {
        new ConfigControlContext<>(property, config, displayName, tooltip).set(value);
    }

    private static <T> boolean addCustomControlPropertyToBuilder(MutableLoadedConfig config, ConfiguredProperty<T> property, ConfigCategory categoryInstance, Component displayName, Component tooltip) {
        final var customControlId = property.customControl().orElse(null);
        if (customControlId == null) {
            return false;
        }

        final var entry = ConfigControlRegistry.createElement(customControlId, new ConfigControlContext<>(property, config, displayName, tooltip)).orElse(null);
        switch (entry) {
            case null -> {
                return false;
            }
            case AbstractConfigListEntry<?> configEntry -> {
                categoryInstance.addEntry(configEntry);
                return true;
            }
            case AbstractWidget widget -> {
                categoryInstance.addEntry(new ClothConfigWidgetConfigListEntry<>(new ConfigControlContext<>(property, config, displayName, tooltip), widget));
                return true;
            }
            default ->
                    throw new IllegalStateException("Config control for " + property.parentSchema().identifier() + "/" + property.category() + "." + property.name() + " must return AbstractConfigListEntry or AbstractWidget for Cloth Config, got " + entry.getClass().getName());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> void addEnumPropertyToBuilder(MutableLoadedConfig config, ConfiguredEnum<T> property, ConfigCategory categoryInstance, ConfigBuilder builder, Component displayName, Component tooltip) {
        categoryInstance.addEntry(
                builder.entryBuilder()
                        .startEnumSelector(displayName, (Class<T>) property.type(), property.get(config))
                        .setDefaultValue(property.defaultValue())
                        .setTooltip(tooltip)
                        .setSaveConsumer(value -> setValue(config, property, displayName, tooltip, value))
                        .build()
        );
    }
}
