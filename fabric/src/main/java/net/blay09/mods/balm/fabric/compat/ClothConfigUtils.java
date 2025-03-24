package net.blay09.mods.balm.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.BalmConfigProperty;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;

public class ClothConfigUtils {
    public static <T extends BalmConfigData> ConfigScreenFactory<?> getConfigScreen(Class<T> clazz) {
        return (ConfigScreenFactory<Screen>) screen -> {
            var configName = Balm.getConfig().getConfigName(clazz);
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(screen)
                    .setTitle(Component.translatable("config." + configName + ".title"));
            builder.setSavingRunnable(() -> Balm.getConfig().saveBackingConfig(clazz));

            var properties = Balm.getConfig().getConfigProperties(clazz);
            for (String category : properties.rowKeySet()) {
                var categoryI18nBase = category.isEmpty() ? "config." + configName : "config." + configName + "." + category;
                var categoryDisplayName = Component.translatable(categoryI18nBase);
                ConfigCategory categoryInstance = builder.getOrCreateCategory(categoryDisplayName);
                for (Map.Entry<String, BalmConfigProperty<?>> entry : properties.row(category).entrySet()) {
                    var property = entry.getKey();
                    var displayName = Component.translatable(categoryI18nBase + "." + property);
                    var tooltip = Component.translatable(categoryI18nBase + "." + property + ".tooltip");
                    var backingProperty = entry.getValue();
                    if (backingProperty.getType() == String.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startStrField(displayName, (String) backingProperty.getValue())
                                        .setDefaultValue((String) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<String>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (backingProperty.getType() == Integer.class || backingProperty.getType() == Integer.TYPE) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startIntField(displayName, (Integer) backingProperty.getValue())
                                        .setDefaultValue((Integer) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<Integer>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (backingProperty.getType() == Long.class || backingProperty.getType() == Long.TYPE) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startLongField(displayName, (Long) backingProperty.getValue())
                                        .setDefaultValue((Long) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<Long>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (backingProperty.getType() == Float.class || backingProperty.getType() == Float.TYPE) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startFloatField(displayName, (Float) backingProperty.getValue())
                                        .setDefaultValue((Float) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<Float>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (backingProperty.getType() == Double.class || backingProperty.getType() == Double.TYPE) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startDoubleField(displayName, (Double) backingProperty.getValue())
                                        .setDefaultValue((Double) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<Double>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (backingProperty.getType() == Boolean.class || backingProperty.getType() == Boolean.TYPE) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startBooleanToggle(displayName, (Boolean) backingProperty.getValue())
                                        .setDefaultValue((Boolean) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<Boolean>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (Enum.class.isAssignableFrom(backingProperty.getType())) {
                        categoryInstance.addEntry(
                                builder.entryBuilder()
                                        .startEnumSelector(displayName, (Class<Enum<?>>) backingProperty.getType(), (Enum<?>) backingProperty.getValue())
                                        .setDefaultValue((Enum<?>) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<Enum<?>>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (List.class.isAssignableFrom(backingProperty.getType()) && backingProperty.getInnerType() == String.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startStrList(displayName, (List<String>) backingProperty.getValue())
                                        .setDefaultValue((List<String>) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<List<String>>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (List.class.isAssignableFrom(backingProperty.getType()) && backingProperty.getInnerType() == Integer.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startIntList(displayName, (List<Integer>) backingProperty.getValue())
                                        .setDefaultValue((List<Integer>) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<List<Integer>>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (List.class.isAssignableFrom(backingProperty.getType()) && backingProperty.getInnerType() == Long.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startLongList(displayName, (List<Long>) backingProperty.getValue())
                                        .setDefaultValue((List<Long>) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<List<Long>>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (List.class.isAssignableFrom(backingProperty.getType()) && backingProperty.getInnerType() == Float.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startFloatList(displayName, (List<Float>) backingProperty.getValue())
                                        .setDefaultValue((List<Float>) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<List<Float>>) backingProperty).setValue(value))
                                        .build()
                        );
                    } else if (List.class.isAssignableFrom(backingProperty.getType()) && backingProperty.getInnerType() == Double.class) {
                        categoryInstance.addEntry(
                                builder.entryBuilder().startDoubleList(displayName, (List<Double>) backingProperty.getValue())
                                        .setDefaultValue((List<Double>) backingProperty.getDefaultValue())
                                        .setTooltip(tooltip)
                                        .setSaveConsumer(value -> ((BalmConfigProperty<List<Double>>) backingProperty).setValue(value))
                                        .build()
                        );
                    }
                }
            }

            return builder.build();
        };
    }
}
