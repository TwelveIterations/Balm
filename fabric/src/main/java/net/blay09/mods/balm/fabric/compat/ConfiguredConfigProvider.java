package net.blay09.mods.balm.fabric.compat;

import com.mrcrayfish.configured.api.*;
import com.mrcrayfish.configured.api.util.ConfigScreenHelper;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ConfiguredConfigProvider implements IModConfigProvider {
    @Override
    public Set<IModConfig> getConfigurationsForMod(ModContext modContext) {
        final var configs = Balm.getConfig().getSchemasByNamespace(modContext.modId());
        return configs.stream()
                .map(schema -> mapConfig(schema, Balm.getConfig().getLocalConfig(schema)))
                .collect(Collectors.toSet());
    }

    private static IModConfig mapConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        return new IModConfig() {
            @Override
            public ActionResult update(IConfigEntry entry) {
                Balm.getConfig().saveLocalConfig(schema, config);
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
                final var modId = schema.identifier().getNamespace();
                return modId + "-common.toml";
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
            public IConfigValue<?> getValue() {
                return null;
            }

            @Override
            public String getEntryName() {
                return "";
            }

            @Override
            public Component getTooltip() {
                return null;
            }

            @Override
            public String getTranslationKey() {
                final var configIdentifier = schema.identifier();
                return "config." + configIdentifier.getNamespace() + "." + configIdentifier.getPath() + ".title";
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
            public IConfigValue<?> getValue() {
                return null;
            }

            @Override
            public String getEntryName() {
                return category.name();
            }

            @Override
            public Component getTooltip() {
                return null;
            }

            @Override
            public String getTranslationKey() {
                final var configIdentifier = category.parentSchema().identifier();
                return "config." + configIdentifier.getNamespace() + "." + configIdentifier.getPath() + "." + category;
            }
        };
    }

    private static <T> IConfigEntry mapConfigProperty(MutableLoadedConfig config, ConfiguredProperty<T> property) {
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
                        config.setRaw(property, o);
                    }

                    @Override
                    public boolean isValid(T o) {
                        return ClassUtils.isAssignable(o.getClass(), property.type(), true);
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
                        final var category = property.category();
                        final var modId = property.parentSchema().identifier();
                        final var key = property.name();
                        return category.isEmpty() ? Component.translatable("config." + modId + "." + key + ".tooltip") : Component.translatable("config." + modId + "." + category + "." + key + ".tooltip");
                    }

                    @Override
                    public String getTranslationKey() {
                        final var category = property.category();
                        final var modId = property.parentSchema().identifier();
                        final var key = property.name();
                        return category.isEmpty() ? "config." + modId + "." + key : "config." + modId + "." + category + "." + key;
                    }

                    @Override
                    public Component getValidationHint() {
                        return null;
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
                final var category = property.category();
                final var modId = property.parentSchema().identifier();
                final var key = property.name();
                return category.isEmpty() ? Component.translatable("config." + modId + "." + key + ".tooltip") : Component.translatable("config." + modId + "." + category + "." + key + ".tooltip");
            }

            @Override
            public String getTranslationKey() {
                final var category = property.category();
                final var modId = property.parentSchema().identifier();
                final var key = property.name();
                return category.isEmpty() ? "config." + modId + "." + key : "config." + modId + "." + category + "." + key;
            }
        };
    }

    public static Screen createConfigScreen(String modId, Screen parent) {
        final var configs = Balm.getConfig().getSchemasByNamespace(modId);
        final var configsByType = new HashMap<ConfigType, Set<IModConfig>>();
        final var mappedConfigs = configs.stream().map(schema -> mapConfig(schema, Balm.getConfig().getLocalConfig(schema))).collect(Collectors.toSet());
        configsByType.put(ConfigType.UNIVERSAL, mappedConfigs);
        return ConfigScreenHelper.createSelectionScreen(parent,
                Component.translatable("config." + modId + ".title"),
                configsByType
        );
    }
}
