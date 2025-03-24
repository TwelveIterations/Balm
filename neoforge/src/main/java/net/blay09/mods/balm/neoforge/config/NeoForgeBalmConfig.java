package net.blay09.mods.balm.neoforge.config;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.*;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeoForgeBalmConfig extends AbstractBalmConfig {

    private final Map<ResourceLocation, Table<String, String, ModConfigSpec.ConfigValue<?>>> properties = new HashMap<>();
    private final Map<ResourceLocation, ModConfig> modConfigs = new HashMap<>();

    private static ModConfigSpec.ConfigValue<?> addPropertyToSpec(BalmConfigSchema schema, ConfiguredProperty<?> property, ModConfigSpec.Builder spec) {
        spec.comment(property.comment())
                .translation("config." + schema.identifier().getNamespace() + "." + schema.identifier().getPath() + "." + property.name());

        return switch (property) {
            case ConfiguredBoolean configuredBoolean -> spec.define(configuredBoolean.name(), configuredBoolean.defaultValue());
            case ConfiguredDouble configuredDouble -> spec.define(configuredDouble.name(), configuredDouble.defaultValue());
            case ConfiguredEnum<?> configuredEnum -> spec.defineEnum(configuredEnum.name(), configuredEnum.defaultValue(), EnumGetMethod.NAME_IGNORECASE);
            case ConfiguredFloat configuredFloat -> spec.define(configuredFloat.name(), configuredFloat.defaultValue());
            case ConfiguredInt configuredInt -> spec.define(configuredInt.name(), configuredInt.defaultValue());
            case ConfiguredList<?> configuredList -> spec.defineListAllowEmpty(configuredList.name(),
                    configuredList.defaultValue(),
                    () -> newListElement(configuredList),
                    (it) -> validateListElement(configuredList, it));
            case ConfiguredLong configuredLong -> spec.define(configuredLong.name(), configuredLong.defaultValue());
            case ConfiguredResourceLocation configuredResourceLocation ->
                    spec.define(configuredResourceLocation.name(), configuredResourceLocation.defaultValue().toString());
            case ConfiguredSet<?> configuredSet -> spec.defineListAllowEmpty(configuredSet.name(),
                    List.copyOf(configuredSet.defaultValue()),
                    () -> newSetElement(configuredSet),
                    (it) -> validateSetElement(configuredSet, it));
            case ConfiguredString configuredString -> spec.define(configuredString.name(), configuredString.defaultValue());
            default -> throw new IllegalStateException("Unexpected value: " + property);
        };
    }

    private static <T> T newListElement(ConfiguredList<T> configuredList) {
        return newCollectionElement(configuredList.nestedType());
    }

    private static <T> T newSetElement(ConfiguredSet<T> configuredSet) {
        return newCollectionElement(configuredSet.nestedType());
    }

    @SuppressWarnings("unchecked")
    private static <T> T newCollectionElement(Class<T> nestedType) {
        if (nestedType == Boolean.class) {
            return (T) Boolean.FALSE;
        } else if (nestedType == Double.class) {
            return (T) Double.valueOf(0.0);
        } else if (nestedType == Float.class) {
            return (T) Float.valueOf(0.0f);
        } else if (nestedType == Integer.class) {
            return (T) Integer.valueOf(0);
        } else if (nestedType == Long.class) {
            return (T) Long.valueOf(0L);
        } else if (nestedType == ResourceLocation.class) {
            return (T) ResourceLocation.fromNamespaceAndPath("minecraft", "air").toString();
        } else if (nestedType == String.class) {
            return (T) "";
        } else if (nestedType.isEnum()) {
            return nestedType.getEnumConstants()[0];
        } else {
            throw new IllegalArgumentException("Unsupported type " + nestedType);
        }
    }

    private static <T> boolean validateListElement(ConfiguredList<T> configuredList, Object value) {
        return validateCollectionElement(configuredList.nestedType(), value);
    }

    private static <T> boolean validateSetElement(ConfiguredSet<T> configuredSet, Object value) {
        return validateCollectionElement(configuredSet.nestedType(), value);
    }

    private static <T> boolean validateCollectionElement(Class<T> nestedType, Object value) {
        if (nestedType == Boolean.class) {
            return value instanceof Boolean || ("true".equals(value) || "false".equals(value));
        } else if (nestedType == Double.class) {
            try {
                return value instanceof Double || !Double.isNaN(Double.parseDouble(value.toString()));
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (nestedType == Float.class) {
            try {
                return value instanceof Float || !Float.isNaN(Float.parseFloat(value.toString()));
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (nestedType == Integer.class) {
            try {
                if (value instanceof Integer) {
                    return true;
                }

                Integer.parseInt(value.toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (nestedType == Long.class) {
            try {
                if (value instanceof Long) {
                    return true;
                }

                Long.parseLong(value.toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (nestedType == ResourceLocation.class) {
            return value instanceof String && ResourceLocation.tryParse(value.toString()) != null;
        } else if (nestedType == String.class) {
            return value instanceof String;
        } else if (nestedType.isEnum()) {
            return value instanceof String && validateEnum(value, nestedType);
        } else {
            throw new IllegalArgumentException("Unsupported type " + nestedType);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> boolean validateEnum(Object value, Class<?> unknownClass) {
        if (unknownClass.isEnum()) {
            return EnumGetMethod.NAME_IGNORECASE.validate(value, (Class<T>) unknownClass);
        } else {
            throw new IllegalArgumentException("Not an enum class: " + unknownClass.getName());
        }
    }

    @Override
    public File getConfigDir() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        super.registerConfig(schema);

        final var modContainer = ModList.get().getModContainerById(schema.identifier().getNamespace())
                .orElseThrow(() -> new IllegalStateException("Mod container for " + schema.identifier()
                        .getNamespace() + " not found when registering config."));
        final var eventBus = modContainer.getEventBus();
        eventBus.addListener((ModConfigEvent.Loading event) -> {
            final var modConfig = event.getConfig();
            final var identifier = ResourceLocation.fromNamespaceAndPath(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals(identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                final var wrappedConfig = new LoadedTableConfig(); // TODO 1.21.5 Configs
                setLocalConfig(schema, wrappedConfig);
                setActiveConfig(schema, wrappedConfig);
            }
        });
        eventBus.addListener((ModConfigEvent.Reloading event) -> {
            final var modConfig = event.getConfig();
            final var identifier = ResourceLocation.fromNamespaceAndPath(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals(identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                final var wrappedConfig = new LoadedTableConfig(); // TODO 1.21.5 Configs
                setLocalConfig(schema, wrappedConfig);
                updateActiveFromLocal(schema, wrappedConfig);

                Balm.getEvents().fireEvent(new ConfigReloadedEvent(schema));
            }
        });

        final var stringType = schema.identifier().getPath();
        final var configType = switch (stringType) {
            case "common" -> ModConfig.Type.COMMON;
            case "client" -> ModConfig.Type.CLIENT;
            default -> throw new IllegalArgumentException("Unsupported config type: " + stringType + " - only 'common' and 'client' are supported.");
        };
        final var mappedConfigSpec = mapToConfigSpec(schema);
        modContainer.registerConfig(configType, mappedConfigSpec.getFirst());
        properties.put(schema.identifier(), mappedConfigSpec.getSecond());

        if (FMLEnvironment.dist == Dist.CLIENT) {
            initializeConfigurationScreen(modContainer);
        }
    }

    @Override
    public void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        super.saveLocalConfig(schema, config);
        final var modConfig = modConfigs.get(schema.identifier());
        if (modConfig == null) {
            throw new IllegalStateException("Backing config not available for " + schema.identifier());
        }
        final var wrappedConfig = new LoadedTableConfig(); // TODO 1.21.5 Configs
        wrappedConfig.applyFrom(schema, config);
        ((ModConfigSpec) modConfig.getSpec()).save();
    }

    private void initializeConfigurationScreen(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private Pair<ModConfigSpec, HashBasedTable<String, String, ModConfigSpec.ConfigValue<?>>> mapToConfigSpec(BalmConfigSchema schema) {
        final var spec = new ModConfigSpec.Builder();
        final var properties = HashBasedTable.<String, String, ModConfigSpec.ConfigValue<?>>create();
        for (final var rootProperty : schema.rootProperties()) {
            properties.put("", rootProperty.name(), addPropertyToSpec(schema, rootProperty, spec));
        }
        for (final var category : schema.categories()) {
            spec.push(category.name());
            for (final var property : category.properties()) {
                properties.put(category.name(), property.name(), addPropertyToSpec(schema, property, spec));
            }
            spec.pop();
        }
        return Pair.of(spec.build(), properties);
    }
}
