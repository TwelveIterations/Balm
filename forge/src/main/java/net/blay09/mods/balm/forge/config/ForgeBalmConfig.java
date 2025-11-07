package net.blay09.mods.balm.forge.config;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.*;
import net.blay09.mods.balm.api.event.ConfigLoadedEvent;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.blay09.mods.balm.common.config.ConfigLocalization;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ForgeBalmConfig extends AbstractBalmConfig {

    private static final Map<ResourceLocation, Table<String, String, ForgeConfigSpec.ConfigValue<?>>> properties = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, ModConfig> modConfigs = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ForgeBalmConfig.class);

    private static ForgeConfigSpec.ConfigValue<?> addPropertyToSpec(ConfiguredProperty<?> property, ForgeConfigSpec.Builder spec) {
        spec.comment(property.comment());
        spec.translation(ConfigLocalization.forProperty(property));

        return switch (property) {
            case ConfiguredBoolean configuredBoolean -> spec.define(configuredBoolean.name(), configuredBoolean.defaultValue().booleanValue());
            case ConfiguredDouble configuredDouble -> spec.define(configuredDouble.name(), configuredDouble.defaultValue());
            case ConfiguredEnum<?> configuredEnum -> defineEnum(spec, configuredEnum);
            case ConfiguredFloat configuredFloat -> spec.define(configuredFloat.name(), configuredFloat.defaultValue().doubleValue());
            case ConfiguredInt configuredInt -> spec.define(configuredInt.name(), configuredInt.defaultValue());
            case ConfiguredList<?> configuredList -> spec.defineListAllowEmpty(configuredList.name(),
                    mapConfigCollectionToNeoForge(configuredList.defaultValue()),
                    (it) -> validateListElement(configuredList, it));
            case ConfiguredLong configuredLong -> spec.define(configuredLong.name(), configuredLong.defaultValue());
            case ConfiguredResourceLocation configuredResourceLocation ->
                    spec.define(configuredResourceLocation.name(), configuredResourceLocation.defaultValue().toString());
            case ConfiguredSet<?> configuredSet -> spec.defineListAllowEmpty(configuredSet.name(),
                    mapConfigCollectionToNeoForge(configuredSet.defaultValue()),
                    (it) -> validateSetElement(configuredSet, it));
            case ConfiguredString configuredString -> spec.define(configuredString.name(), configuredString.defaultValue());
            default -> throw new IllegalStateException("Unexpected value: " + property);
        };
    }

    public static List<?> mapConfigCollectionToNeoForge(Collection<?> values) {
        return values.stream().map(ForgeBalmConfig::mapConfigValueToNeoForge).toList();
    }

    public static Object mapConfigValueToNeoForge(Object value) {
        return switch (value) {
            case ResourceLocation resourceLocation -> resourceLocation.toString();
            case Float floatValue -> floatValue.doubleValue();
            case Set<?> setValue -> mapConfigCollectionToNeoForge(setValue);
            case List<?> listValue -> mapConfigCollectionToNeoForge(listValue);
            case null, default -> value;
        };
    }

    public static List<?> mapConfigListFromNeoForge(ConfiguredList<?> property, List<?> value) {
        return value.stream().map(it -> mapConfigValueFromNeoForge(property.nestedType(), it)).toList();
    }

    public static Set<?> mapConfigSetFromNeoForge(ConfiguredSet<?> property, List<?> value) {
        return value.stream().map(it -> mapConfigValueFromNeoForge(property.nestedType(), it)).collect(Collectors.toSet());
    }

    public static Object mapConfigValueFromNeoForge(ConfiguredProperty<?> property, Object value) {
        return switch (property) {
            case ConfiguredResourceLocation ignored -> ResourceLocation.parse((String) value);
            case ConfiguredFloat ignored -> ((Double) value).floatValue();
            case ConfiguredList<?> configuredList -> mapConfigListFromNeoForge(configuredList, (List<?>) value);
            case ConfiguredSet<?> configuredSet -> mapConfigSetFromNeoForge(configuredSet, (List<?>) value);
            case null, default -> value;
        };
    }

    private static Object mapConfigValueFromNeoForge(Class<?> nestedType, Object value) {
        if (nestedType == ResourceLocation.class) {
            return ResourceLocation.parse((String) value);
        } else if (nestedType == Float.class) {
            return ((Double) value).floatValue();
        } else if (nestedType.isEnum() && value instanceof String) {
            return stringToEnum(value, nestedType);
        } else {
            return value;
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

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> T stringToEnum(Object value, Class<?> unknownClass) {
        if (unknownClass.isEnum()) {
            return EnumGetMethod.NAME_IGNORECASE.get(value, (Class<T>) unknownClass);
        } else {
            throw new IllegalArgumentException("Not an enum class: " + unknownClass.getName());
        }
    }

    private static <T extends Enum<T>> ForgeConfigSpec.ConfigValue<T> defineEnum(ForgeConfigSpec.Builder spec, ConfiguredEnum<T> configuredEnum) {
        return spec.defineEnum(configuredEnum.name(), configuredEnum.defaultValue(), EnumGetMethod.NAME_IGNORECASE);
    }

    @Override
    public File getConfigDir() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        super.registerConfig(schema);

        final var namespace = schema.identifier().getNamespace();
        final var modContainer = ModList.get().getModContainerById(namespace)
                .orElseThrow(() -> new IllegalStateException("Mod container for " + namespace + " not found when registering config."));
        final var modBusGroup = BalmLoadContexts.get(namespace).map(it -> ((ForgeLoadContext) it).modBusGroup()).orElse(null);
        if (modBusGroup == null) {
            throw new IllegalStateException("Missing event bus group for " + namespace + " when registering config.");
        }

        ModConfigEvent.Loading.getBus(modBusGroup).addListener((event) -> {
            final var modConfig = event.getConfig();
            final var identifier = ResourceLocation.fromNamespaceAndPath(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals(identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                final var modConfigProperties = properties.get(schema.identifier());
                if (modConfigProperties == null) {
                    throw new IllegalStateException("Missing config properties for " + schema.identifier() + " when loading config. Properties present: " + properties.keySet());
                }
                final var wrappedConfig = new LoadedForgeConfig(schema, modConfig, modConfigProperties);
                setLocalConfig(schema, wrappedConfig);
                setActiveConfig(schema, wrappedConfig);

                fireConfigLoadHandlers(schema, wrappedConfig);
                Balm.getEvents().fireEvent(new ConfigLoadedEvent(schema));
            }
        });
        ModConfigEvent.Reloading.getBus(modBusGroup).addListener((event) -> {
            final var modConfig = event.getConfig();
            final var identifier = ResourceLocation.fromNamespaceAndPath(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals(identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                final var modConfigProperties = properties.get(schema.identifier());
                if (modConfigProperties == null) {
                    throw new IllegalStateException("Missing config properties for " + schema.identifier() + " when loading config. Properties present: " + properties.keySet());
                }
                final var wrappedConfig = new LoadedForgeConfig(schema, modConfig, modConfigProperties);
                setLocalConfig(schema, wrappedConfig);
                updateActiveFromLocal(schema, wrappedConfig);

                Balm.getEvents().fireEvent(new ConfigReloadedEvent(schema));
            }
        });

        final var stringType = schema.identifier().getPath();
        final var configType = switch (stringType) {
            case "common" -> ModConfig.Type.COMMON;
            case "client" -> ModConfig.Type.CLIENT;
            case "server" -> ModConfig.Type.SERVER;
            default -> throw new IllegalArgumentException("Unsupported config type: " + stringType + " - only 'common' and 'client' are supported.");
        };
        final var mappedConfigSpec = mapToConfigSpec(schema);
        properties.put(schema.identifier(), mappedConfigSpec.getSecond());
        logger.info("Registering config for {} ({}) with {} properties.", schema.identifier(), configType, mappedConfigSpec.getSecond().size());
        modContainer.addConfig(new ModConfig(configType, mappedConfigSpec.getFirst(), modContainer));
    }

    @Override
    public void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        super.saveLocalConfig(schema, config);
        final var modConfig = modConfigs.get(schema.identifier());
        if (modConfig == null) {
            throw new IllegalStateException("Backing config not available for " + schema.identifier());
        }
        final var wrappedConfig = new LoadedForgeConfig(schema, modConfig, properties.get(schema.identifier()));
        wrappedConfig.applyFrom(schema, config);
        ((ForgeConfigSpec) modConfig.getSpec()).save();
    }

    private Pair<ForgeConfigSpec, HashBasedTable<String, String, ForgeConfigSpec.ConfigValue<?>>> mapToConfigSpec(BalmConfigSchema schema) {
        final var spec = new ForgeConfigSpec.Builder();
        final var properties = HashBasedTable.<String, String, ForgeConfigSpec.ConfigValue<?>>create();
        for (final var rootProperty : schema.rootProperties()) {
            properties.put("", rootProperty.name(), addPropertyToSpec(rootProperty, spec));
        }
        for (final var category : schema.categories()) {
            spec.push(category.name());
            for (final var property : category.properties()) {
                properties.put(category.name(), property.name(), addPropertyToSpec(property, spec));
            }
            spec.pop();
        }
        return Pair.of(spec.build(), properties);
    }
}
