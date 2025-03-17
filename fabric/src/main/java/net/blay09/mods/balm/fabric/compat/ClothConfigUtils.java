package net.blay09.mods.balm.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ClothConfigUtils {
    public static ConfigScreenFactory<?> getConfigScreen(String modId) {
        return (ConfigScreenFactory<Screen>) screen -> {
            final var builder = ConfigBuilder.create()
                    .setParentScreen(screen)
                    .setTitle(Component.translatable("config." + modId + ".title"));
            final var schemas = Balm.getConfig().getSchemasByNamespace(modId);
            builder.setSavingRunnable(() -> {
                for (final var schema : schemas) {
                    Balm.getConfig().saveLocalConfig(schema, Balm.getConfig().getLocalConfig(schema));
                }
            });
            for (final var schema : schemas) {
                final var config = Balm.getConfig().getLocalConfig(schema);
                final var i18nBase = "config." + schema.identifier().getNamespace() + "." + schema.identifier().getPath();
                final var categories = schema.categories();
                ConfigCategory rootCategory = null;
                for (final var rootProperty : schema.rootProperties()) {
                    if (rootCategory == null) {
                        rootCategory = builder.getOrCreateCategory(Component.translatable(i18nBase));
                    }
                    addPropertyToBuilder(config, rootProperty, rootCategory, builder);
                }
                for (final var category : categories) {
                    var categoryI18nBase = i18nBase + "." + category;
                    final var categoryInstance = builder.getOrCreateCategory(Component.translatable(categoryI18nBase));
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
        final var schema = property.parentSchema();
        final var i18nBase = "config." + schema.identifier().getNamespace() + "." + schema.identifier().getPath();
        var categoryI18nBase = i18nBase + "." + property.category();
        var displayName = Component.translatable(categoryI18nBase + "." + property);
        var tooltip = Component.translatable(categoryI18nBase + "." + property + ".tooltip");
        switch (property) {
            case ConfiguredString stringProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrField(displayName, stringProperty.get(config))
                            .setDefaultValue(stringProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> stringProperty.set(config, value))
                            .build()
            );
            case ConfiguredInt intProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startIntField(displayName, intProperty.get(config))
                            .setDefaultValue(intProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> intProperty.set(config, value))
                            .build()
            );
            case ConfiguredFloat floatProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startFloatField(displayName, floatProperty.get(config))
                            .setDefaultValue(floatProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> floatProperty.set(config, value))
                            .build()
            );
            case ConfiguredBoolean booleanProperty -> categoryInstance.addEntry(
                    builder.entryBuilder().startBooleanToggle(displayName, booleanProperty.get(config))
                            .setDefaultValue(booleanProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> booleanProperty.set(config, value))
                            .build()
            );
            case ConfiguredEnum<?> enumProperty -> addEnumPropertyToBuilder(config, enumProperty, categoryInstance, builder, displayName, tooltip);
            case ConfiguredList<?> listProperty when listProperty.nestedType() == String.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrList(displayName, (List<String>) listProperty.get(config))
                            .setDefaultValue((List<String>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredList<String>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Integer.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startIntList(displayName, (List<Integer>) listProperty.get(config))
                            .setDefaultValue((List<Integer>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredList<Integer>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Long.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startLongList(displayName, (List<Long>) listProperty.get(config))
                            .setDefaultValue((List<Long>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredList<Long>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Float.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startFloatList(displayName, (List<Float>) listProperty.get(config))
                            .setDefaultValue((List<Float>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredList<Float>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredList<?> listProperty when listProperty.nestedType() == Double.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startDoubleList(displayName, (List<Double>) listProperty.get(config))
                            .setDefaultValue((List<Double>) listProperty.defaultValue())
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredList<Double>) listProperty).set(config, value))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == String.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startStrList(displayName, new ArrayList<>((Set<String>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<String>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredSet<String>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Integer.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startIntList(displayName, new ArrayList<>((Set<Integer>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Integer>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredSet<Integer>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Long.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startLongList(displayName, new ArrayList<>((Set<Long>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Long>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredSet<Long>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Float.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startFloatList(displayName, new ArrayList<>((Set<Float>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Float>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredSet<Float>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            case ConfiguredSet<?> setProperty when setProperty.nestedType() == Double.class -> categoryInstance.addEntry(
                    builder.entryBuilder().startDoubleList(displayName, new ArrayList<>((Set<Double>) setProperty.get(config)))
                            .setDefaultValue(new ArrayList<>((Set<Double>) setProperty.defaultValue()))
                            .setTooltip(tooltip)
                            .setSaveConsumer(value -> ((ConfiguredSet<Double>) setProperty).set(config, new HashSet<>(value)))
                            .build()
            );
            default -> {
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
                        .setSaveConsumer(value -> property.set(config, value))
                        .build()
        );
    }
}
