package net.blay09.mods.balm.neoforge.config;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.*;
import net.blay09.mods.balm.api.event.ConfigLoadedEvent;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.blay09.mods.balm.common.config.ConfigLocalization;
import net.minecraft.resources.Identifier;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmSupplementalEvents;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NeoForgeBalmConfig extends AbstractBalmConfig {

    private static final Map<Identifier, Table<String, String, ModConfigSpec.ConfigValue<?>>> properties = new ConcurrentHashMap<>();
    private static final Map<Identifier, ModConfig> modConfigs = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(NeoForgeBalmConfig.class);

    private static ModConfigSpec.ConfigValue<?> addPropertyToSpec(ConfiguredProperty<?> property, ModConfigSpec.Builder spec) {
        if (!property.comment().isBlank()) {
            spec.comment(property.comment());
        }
        spec.translation(ConfigLocalization.forProperty(property));

        return switch (property) {
            case ConfiguredBoolean configuredBoolean ->
                    spec.define(configuredBoolean.name(), configuredBoolean.defaultValue().booleanValue());
            case ConfiguredDouble configuredDouble ->
                    spec.define(configuredDouble.name(), configuredDouble.defaultValue());
            case ConfiguredEnum<?> configuredEnum -> defineEnum(spec, configuredEnum);
            case ConfiguredFloat configuredFloat ->
                    spec.define(configuredFloat.name(), configuredFloat.defaultValue().doubleValue());
            case ConfiguredInt configuredInt -> spec.define(configuredInt.name(), configuredInt.defaultValue());
            case ConfiguredList<?> configuredList -> spec.defineListAllowEmpty(configuredList.name(),
                    mapConfigCollectionToNeoForge(configuredList.defaultValue()),
                    () -> newListElement(configuredList),
                    (it) -> validateListElement(configuredList, it));
            case ConfiguredLong configuredLong -> spec.define(configuredLong.name(), configuredLong.defaultValue());
            case ConfiguredIdentifier configuredIdentifier ->
                    spec.define(configuredIdentifier.name(), configuredIdentifier.defaultValue().toString());
            case ConfiguredSet<?> configuredSet -> spec.defineListAllowEmpty(configuredSet.name(),
                    mapConfigCollectionToNeoForge(configuredSet.defaultValue()),
                    () -> newSetElement(configuredSet),
                    (it) -> validateSetElement(configuredSet, it));
            case ConfiguredString configuredString ->
                    spec.define(configuredString.name(), configuredString.defaultValue());
            default -> throw new IllegalStateException("Unexpected value: " + property);
        };
    }

    public static List<?> mapConfigCollectionToNeoForge(Collection<?> values) {
        return values.stream().map(NeoForgeBalmConfig::mapConfigValueToNeoForge).toList();
    }

    public static Object mapConfigValueToNeoForge(Object value) {
        return switch (value) {
            case Identifier identifier -> identifier.toString();
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
            case ConfiguredIdentifier ignored -> Identifier.parse((String) value);
            case ConfiguredFloat ignored -> ((Double) value).floatValue();
            case ConfiguredList<?> configuredList -> mapConfigListFromNeoForge(configuredList, (List<?>) value);
            case ConfiguredSet<?> configuredSet -> mapConfigSetFromNeoForge(configuredSet, (List<?>) value);
            case null, default -> value;
        };
    }

    private static Object mapConfigValueFromNeoForge(Class<?> nestedType, Object value) {
        if (nestedType == Identifier.class) {
            return Identifier.parse((String) value);
        } else if (nestedType == Float.class) {
            return ((Double) value).floatValue();
        } else if (nestedType.isEnum() && value instanceof String) {
            return stringToEnum(value, nestedType);
        } else {
            return value;
        }
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
            return (T) Double.valueOf(0.0f);
        } else if (nestedType == Integer.class) {
            return (T) Integer.valueOf(0);
        } else if (nestedType == Long.class) {
            return (T) Long.valueOf(0L);
        } else if (nestedType == Identifier.class) {
            return (T) Identifier.fromNamespaceAndPath("minecraft", "air").toString();
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
        } else if (nestedType == Identifier.class) {
            return value instanceof String && Identifier.tryParse(value.toString()) != null;
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

    private static <T extends Enum<T>> ModConfigSpec.ConfigValue<T> defineEnum(ModConfigSpec.Builder spec, ConfiguredEnum<T> configuredEnum) {
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
        final var eventBus = modContainer.getEventBus();
        if (eventBus == null) {
            throw new IllegalStateException("Missing event bus for " + schema.identifier().getNamespace() + " when registering config.");
        }

        eventBus.addListener((ModConfigEvent.Loading event) -> {
            final var modConfig = event.getConfig();
            final var identifier = Identifier.fromNamespaceAndPath(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals(identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                final var modConfigProperties = properties.get(schema.identifier());
                if (modConfigProperties == null) {
                    throw new IllegalStateException("Missing config properties for " + schema.identifier() + " when loading config. Properties present: " + properties.keySet());
                }
                final var wrappedConfig = new LoadedNeoForgeConfig(schema, modConfig, modConfigProperties);
                setLocalConfig(schema, wrappedConfig);
                setActiveConfig(schema, wrappedConfig);

                fireConfigLoadHandlers(schema, wrappedConfig);
                Balm.getEvents().fireEvent(new ConfigLoadedEvent(schema));
                NeoForgeBalmSupplementalEvents.CONFIG_LOADED.invoker().handle(schema);
            }
        });
        eventBus.addListener((ModConfigEvent.Reloading event) -> {
            final var modConfig = event.getConfig();
            final var identifier = Identifier.fromNamespaceAndPath(modConfig.getModId(), modConfig.getType().extension());
            if (schema.identifier().equals(identifier)) {
                modConfigs.put(schema.identifier(), modConfig);
                final var modConfigProperties = properties.get(schema.identifier());
                if (modConfigProperties == null) {
                    throw new IllegalStateException("Missing config properties for " + schema.identifier() + " when loading config. Properties present: " + properties.keySet());
                }
                final var wrappedConfig = new LoadedNeoForgeConfig(schema, modConfig, modConfigProperties);
                setLocalConfig(schema, wrappedConfig);
                updateActiveFromLocal(schema, wrappedConfig);

                Balm.getEvents().fireEvent(new ConfigReloadedEvent(schema));
                NeoForgeBalmSupplementalEvents.CONFIG_RELOADED.invoker().handle(schema);
            }
        });

        final var stringType = schema.identifier().getPath();
        final var configType = switch (stringType) {
            case "common" -> ModConfig.Type.COMMON;
            case "client" -> ModConfig.Type.CLIENT;
            case "server" -> ModConfig.Type.SERVER;
            case "startup" -> ModConfig.Type.STARTUP;
            default ->
                    throw new IllegalArgumentException("Unsupported config type: " + stringType + " - only 'common', 'client' and 'startup' are supported.");
        };
        final var mappedConfigSpec = mapToConfigSpec(schema);
        properties.put(schema.identifier(), mappedConfigSpec.getSecond());
        logger.info("Registering config for {} ({}) with {} properties.", schema.identifier(), configType, mappedConfigSpec.getSecond().size());
        modContainer.registerConfig(configType, mappedConfigSpec.getFirst());

        if (FMLEnvironment.getDist() == Dist.CLIENT) {
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
        final var modConfigProperties = properties.get(schema.identifier());
        if (modConfigProperties == null) {
            throw new IllegalStateException("Missing config properties for " + schema.identifier() + " when loading config. Properties present: " + properties.keySet());
        }

        final var wrappedConfig = new LoadedNeoForgeConfig(schema, modConfig, modConfigProperties);
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
