package net.blay09.mods.balm.platform.compatibility.config.internal;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenFactory;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlContextImpl;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlRegistry;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;
import net.blay09.mods.balm.platform.config.schema.*;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ClothConfigSupport {
    private static final Logger logger = LoggerFactory.getLogger(ClothConfigSupport.class);
    private static final Component INVALID_IDENTIFIER_ERROR = Component.translatable("gui.balm.configuration.validation.invalid_identifier");

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
        if (addCustomControlPropertyToBuilder(config, property, categoryInstance)) {
            return;
        }

        final var label = Component.translatable(ConfigLocalization.forProperty(property));
        final var tooltip = Component.translatable(ConfigLocalization.forPropertyTooltip(property));
        switch (property) {
            case ConfiguredString stringProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrField(label, stringProperty.get(config))
                            .setDefaultValue(stringProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setErrorSupplier(value -> validateOrError(stringProperty, value))
                            .setSaveConsumer(value -> stringProperty.set(config, value))
                            .build()
            );
            case ConfiguredInt intProperty -> {
                var fieldBuilder = builder.entryBuilder().startIntField(label, intProperty.get(config))
                        .setDefaultValue(intProperty.defaultValue())
                        .setTooltip(tooltip)
                        .setErrorSupplier(value -> validateOrError(intProperty, value))
                        .setSaveConsumer(value -> intProperty.set(config, value));
                fieldBuilder = intProperty.minValue().map(fieldBuilder::setMin).orElse(fieldBuilder);
                fieldBuilder = intProperty.maxValue().map(fieldBuilder::setMax).orElse(fieldBuilder);
                categoryInstance.addEntry(fieldBuilder.build());
            }
            case ConfiguredFloat floatProperty -> {
                var fieldBuilder = builder.entryBuilder().startFloatField(label, floatProperty.get(config))
                        .setDefaultValue(floatProperty.defaultValue())
                        .setTooltip(tooltip)
                        .setErrorSupplier(value -> validateOrError(floatProperty, value))
                        .setSaveConsumer(value -> floatProperty.set(config, value));
                fieldBuilder = floatProperty.minValue().map(fieldBuilder::setMin).orElse(fieldBuilder);
                fieldBuilder = floatProperty.maxValue().map(fieldBuilder::setMax).orElse(fieldBuilder);
                categoryInstance.addEntry(fieldBuilder.build());
            }
            case ConfiguredBoolean booleanProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startBooleanToggle(label, booleanProperty.get(config))
                            .setDefaultValue(booleanProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setErrorSupplier(value -> validateOrError(booleanProperty, value))
                            .setSaveConsumer(value -> booleanProperty.set(config, value))
                            .build()
            );
            case ConfiguredEnum<?> enumProperty -> addEnumPropertyToBuilder(config, enumProperty, categoryInstance, builder, label, tooltip);
            case ConfiguredList<?> listProperty when listProperty.nestedType() == String.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrList(label, (List<String>) listProperty.get(config))
                            .setDefaultValue((List<String>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateListElementOrError((ConfiguredList<String>) listProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredList<String>) listProperty, value))
                            .setSaveConsumer(value -> ((ConfiguredList<String>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Integer.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startIntList(label, (List<Integer>) listProperty.get(config))
                            .setDefaultValue((List<Integer>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateListElementOrError((ConfiguredList<Integer>) listProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredList<Integer>) listProperty, value))
                            .setSaveConsumer(value -> ((ConfiguredList<Integer>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Long.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startLongList(label, (List<Long>) listProperty.get(config))
                            .setDefaultValue((List<Long>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateListElementOrError((ConfiguredList<Long>) listProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredList<Long>) listProperty, value))
                            .setSaveConsumer(value -> ((ConfiguredList<Long>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Float.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startFloatList(label, (List<Float>) listProperty.get(config))
                            .setDefaultValue((List<Float>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateListElementOrError((ConfiguredList<Float>) listProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredList<Float>) listProperty, value))
                            .setSaveConsumer(value -> ((ConfiguredList<Float>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Double.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startDoubleList(label, (List<Double>) listProperty.get(config))
                            .setDefaultValue((List<Double>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateListElementOrError((ConfiguredList<Double>) listProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredList<Double>) listProperty, value))
                            .setSaveConsumer(value -> ((ConfiguredList<Double>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Identifier.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrList(label, listProperty.get(config).stream().map(Objects::toString).toList())
                            .setDefaultValue(listProperty.defaultValue().stream().map(Objects::toString).toList())
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateIdentifierListElementOrError((ConfiguredList<Identifier>) listProperty, value))
                            .setErrorSupplier(value -> validateIdentifierListOrError((ConfiguredList<Identifier>) listProperty, value))
                            .setSaveConsumer(value -> ((ConfiguredList<Identifier>) listProperty).set(config, value.stream().map(Identifier::tryParse).filter(Objects::nonNull).collect(Collectors.toList())))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == String.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrList(label, new ArrayList<>((Set<String>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<String>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateSetElementOrError((ConfiguredSet<String>) setProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredSet<String>) setProperty, new HashSet<>(value)))
                            .setSaveConsumer(value -> ((ConfiguredSet<String>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Integer.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startIntList(label, new ArrayList<>((Set<Integer>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Integer>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateSetElementOrError((ConfiguredSet<Integer>) setProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredSet<Integer>) setProperty, new HashSet<>(value)))
                            .setSaveConsumer(value -> ((ConfiguredSet<Integer>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Long.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startLongList(label, new ArrayList<>((Set<Long>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Long>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateSetElementOrError((ConfiguredSet<Long>) setProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredSet<Long>) setProperty, new HashSet<>(value)))
                            .setSaveConsumer(value -> ((ConfiguredSet<Long>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Float.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startFloatList(label, new ArrayList<>((Set<Float>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Float>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateSetElementOrError((ConfiguredSet<Float>) setProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredSet<Float>) setProperty, new HashSet<>(value)))
                            .setSaveConsumer(value -> ((ConfiguredSet<Float>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Double.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startDoubleList(label, new ArrayList<>((Set<Double>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Double>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateSetElementOrError((ConfiguredSet<Double>) setProperty, value))
                            .setErrorSupplier(value -> validateOrError((ConfiguredSet<Double>) setProperty, new HashSet<>(value)))
                            .setSaveConsumer(value -> ((ConfiguredSet<Double>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Identifier.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrList(label, setProperty.get(config).stream().map(Objects::toString).toList())
                            .setDefaultValue(setProperty.defaultValue().stream().map(Objects::toString).toList())
                            .setTooltip(tooltip)
                            .setCellErrorSupplier(value -> validateIdentifierSetElementOrError((ConfiguredSet<Identifier>) setProperty, value))
                            .setErrorSupplier(value -> validateIdentifierSetOrError((ConfiguredSet<Identifier>) setProperty, value))
                            .setSaveConsumer(value -> ((ConfiguredSet<Identifier>) setProperty).set(config, value.stream().map(Identifier::tryParse).filter(Objects::nonNull).collect(Collectors.toSet())))
                            .build()
            );
            default -> {
            }
        }
    }

    private static <T> boolean addCustomControlPropertyToBuilder(MutableLoadedConfig config, ConfiguredProperty<T> property, ConfigCategory categoryInstance) {
        final var customControlId = property.customControl().orElse(null);
        if (customControlId == null) {
            return false;
        }

        final var binding = new ConfigControlBinding<>(property, config);
        final var context = new ConfigControlContextImpl(148, 18, _ -> {
            throw new IllegalArgumentException("No configuration bindings are available in this context");
        });
        final var entry = ConfigControlRegistry.createElement(customControlId, binding, context).orElse(null);
        switch (entry) {
            case null -> {
                return false;
            }
            case AbstractConfigListEntry<?> configEntry -> {
                categoryInstance.addEntry(configEntry);
                return true;
            }
            case AbstractWidget widget -> {
                categoryInstance.addEntry(new ClothConfigWidgetConfigListEntry<>(binding, widget, context.label(property)));
                return true;
            }
            default -> {
                logger.warn("Config control for {}/{}.{} must return AbstractConfigListEntry or AbstractWidget for Cloth Config, got {}. Falling back to default control.",
                        property.parentSchema().identifier(), property.category(), property.name(), entry.getClass().getName());
                return false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> void addEnumPropertyToBuilder(MutableLoadedConfig config, ConfiguredEnum<T> property, ConfigCategory categoryInstance, ConfigBuilder builder, Component displayName, Component tooltip) {
        categoryInstance.addEntry(
                builder.entryBuilder()
                        .startEnumSelector(displayName, (Class<T>) property.type(), property.get(config))
                        .setDefaultValue(property.defaultValue())
                        .setTooltip(tooltip)
                        .setErrorSupplier(value -> validateOrError(property, value))
                        .setSaveConsumer(value -> property.set(config, value))
                        .build()
        );
    }

    private static <T> Optional<Component> validateOrError(ConfiguredProperty<T> property, T value) {
        return property.validateValue(value).error().map(error -> Component.literal(error.message()));
    }

    private static <T> Optional<Component> validateListElementOrError(ConfiguredList<T> property, T value) {
        return property.validateElement(value).error().map(error -> Component.literal(error.message()));
    }

    private static <T> Optional<Component> validateSetElementOrError(ConfiguredSet<T> property, T value) {
        return property.validateElement(value).error().map(error -> Component.literal(error.message()));
    }

    private static Optional<Component> validateIdentifierListElementOrError(ConfiguredList<Identifier> property, String value) {
        final var identifier = Identifier.tryParse(value);
        return identifier != null ? validateListElementOrError(property, identifier) : Optional.of(INVALID_IDENTIFIER_ERROR);
    }

    private static Optional<Component> validateIdentifierListOrError(ConfiguredList<Identifier> property, List<String> value) {
        final var identifiers = new ArrayList<Identifier>();
        for (final var stringValue : value) {
            final var identifier = Identifier.tryParse(stringValue);
            if (identifier == null) {
                return Optional.of(INVALID_IDENTIFIER_ERROR);
            }
            identifiers.add(identifier);
        }
        return validateOrError(property, identifiers);
    }

    private static Optional<Component> validateIdentifierSetElementOrError(ConfiguredSet<Identifier> property, String value) {
        final var identifier = Identifier.tryParse(value);
        return identifier != null ? validateSetElementOrError(property, identifier) : Optional.of(INVALID_IDENTIFIER_ERROR);
    }

    private static Optional<Component> validateIdentifierSetOrError(ConfiguredSet<Identifier> property, List<String> value) {
        final var identifiers = new HashSet<Identifier>();
        for (final var stringValue : value) {
            final var identifier = Identifier.tryParse(stringValue);
            if (identifier == null) {
                return Optional.of(INVALID_IDENTIFIER_ERROR);
            }
            identifiers.add(identifier);
        }
        return validateOrError(property, identifiers);
    }
}
