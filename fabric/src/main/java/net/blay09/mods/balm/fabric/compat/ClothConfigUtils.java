package net.blay09.mods.balm.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigProperty;
import net.blay09.mods.balm.api.config.v2.schema.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.*;

public class ClothConfigUtils {
    public static ConfigScreenFactory<?> getConfigScreen(BalmConfigSchema schema) {
        return (ConfigScreenFactory<Screen>) screen -> {
            final var config = Balm.getConfig().getLocalConfig(schema);
            final var i18nBase = "config." + schema.identifier().getNamespace() + "." + schema.identifier().getPath();
            final var builder = ConfigBuilder.create()
                    .setParentScreen(screen)
                    .setTitle(Component.translatable(i18nBase + ".title"));
            builder.setSavingRunnable(() -> Balm.getConfig().saveLocalConfig(schema, config));

            final var categories = schema.categories();
            for (final var rootProperty : schema.rootProperties()) {
// TODO
            }
            for (final var category : categories) {
                var categoryI18nBase = i18nBase + "." + category;
                var categoryDisplayName = Component.translatable(categoryI18nBase);
                final var categoryInstance = builder.getOrCreateCategory(categoryDisplayName);
                for (final var property : category.properties()) {
                    var displayName = Component.translatable(categoryI18nBase + "." + property);
                    var tooltip = Component.translatable(categoryI18nBase + "." + property + ".tooltip");
                    if (property instanceof ConfiguredString stringProperty) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startStrField(displayName, stringProperty.get(config))
                                        .setDefaultValue(stringProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> stringProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredInt intProperty) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startIntField(displayName, intProperty.get(config))
                                        .setDefaultValue(intProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> intProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredFloat floatProperty) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startFloatField(displayName, floatProperty.get(config))
                                        .setDefaultValue(floatProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> floatProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredBoolean booleanProperty) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startBooleanToggle(displayName, booleanProperty.get(config))
                                        .setDefaultValue(booleanProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> booleanProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredEnum<?> enumProperty) {
                        categoryInstance.addEntry(
                                builder.entryBuilder()
                                        .startEnumSelector(displayName, property.type(), enumProperty.get(config))
                                        .setDefaultValue(enumProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> enumProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredList<?> listProperty && listProperty.nestedType() == String.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startStrList(displayName, (List<String>) listProperty.get(config))
                                        .setDefaultValue((List<String>) listProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> listProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredList<?> listProperty && listProperty.nestedType() == Integer.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startIntList(displayName, (List<Integer>) listProperty.get(config))
                                        .setDefaultValue((List<Integer>) listProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> listProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredList<?> listProperty && listProperty.nestedType() == Long.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startLongList(displayName, (List<Long>) listProperty.get(config))
                                        .setDefaultValue((List<Long>) listProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> listProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredList<?> listProperty && listProperty.nestedType() == Float.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startFloatList(displayName, (List<Float>) listProperty.get(config))
                                        .setDefaultValue((List<Float>) listProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> listProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredList<?> listProperty && listProperty.nestedType() == Double.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startDoubleList(displayName, (List<Double>) listProperty.get(config))
                                        .setDefaultValue((List<Double>) listProperty.defaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> listProperty.set(config, value))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredSet<?> setProperty && setProperty.nestedType() == String.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startStrList(displayName, new ArrayList<>((Set<String>) setProperty.get(config)))
                                        .setDefaultValue(new ArrayList<>((Set<String>) setProperty.defaultValue()))
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> setProperty.set(config, new HashSet<>(value)))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredSet<?> setProperty && setProperty.nestedType() == Integer.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startIntList(displayName, new ArrayList<>((Set<Integer>) setProperty.get(config)))
                                        .setDefaultValue(new ArrayList<>((Set<Integer>) setProperty.defaultValue()))
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> setProperty.set(config, new HashSet<>(value)))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredSet<?> setProperty && setProperty.nestedType() == Long.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startLongList(displayName, new ArrayList<>((Set<Long>) setProperty.get(config)))
                                        .setDefaultValue(new ArrayList<>((Set<Long>) setProperty.defaultValue()))
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> setProperty.set(config, new HashSet<>(value)))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredSet<?> setProperty && setProperty.nestedType() == Float.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startFloatList(displayName, new ArrayList<>((Set<Float>) setProperty.get(config)))
                                        .setDefaultValue(new ArrayList<>((Set<Float>) setProperty.defaultValue()))
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> setProperty.set(config, new HashSet<>(value)))
                                        .build()
                        );
                    } else if (property instanceof ConfiguredSet<?> setProperty && setProperty.nestedType() == Double.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startDoubleList(displayName, new ArrayList<>((Set<Double>) setProperty.get(config)))
                                        .setDefaultValue(new ArrayList<>((Set<Double>) setProperty.defaultValue()))
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> setProperty.set(config, new HashSet<>(value)))
                                        .build()
                        );
                    }
                }
            }

            return builder.build();
        };
    }
}
