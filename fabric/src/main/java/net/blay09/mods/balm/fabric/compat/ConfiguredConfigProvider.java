package net.blay09.mods.balm.fabric.compat;

import com.mrcrayfish.configured.api.*;
import com.mrcrayfish.configured.api.util.ConfigScreenHelper;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.common.config.ConfigLocalization;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.ClassUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConfiguredConfigProvider implements IModConfigProvider {
    private static IModConfig mapConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        return new IModConfig() {
            @Override
            public void update(IConfigEntry entry) {
                Balm.getConfig().saveLocalConfig(schema, config);
            }

            @Override
            public IConfigEntry getRoot() {
                return mapConfigSchema(schema, config);
            }

            @Override
            public ConfigType getType() {
                return ConfigType.UNIVERSAL;
            }

            @Override
            public String getFileName() {
                return Balm.getConfig().getConfigFile(schema).getName();
            }

            @Override
            public String getModId() {
                return schema.identifier().getNamespace();
            }

            @Override
            public void loadWorldConfig(Path path, Consumer<IModConfig> consumer) {
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
                return ConfigLocalization.forCategory(category);
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
                        return Component.translatable(ConfigLocalization.forPropertyTooltip(property));
                    }

                    @Override
                    public String getTranslationKey() {
                        return ConfigLocalization.forProperty(property);
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
                return Component.translatable(ConfigLocalization.forPropertyTooltip(property));
            }

            @Override
            public String getTranslationKey() {
                return ConfigLocalization.forPropertyTooltip(property);
            }
        };
    }

    public static Screen createConfigScreen(String modId, Screen parent) {
        final var configs = Balm.getConfig().getSchemasByNamespace(modId);
        final var configsByType = new HashMap<ConfigType, Set<IModConfig>>();
        final var universalConfigs = configs.stream()
                .filter(it -> !it.identifier().getPath().equals("client"))
                .map(schema -> mapConfig(schema, Balm.getConfig().getLocalConfig(schema))).collect(Collectors.toSet());
        final var clientConfigs = configs.stream()
                .filter(it -> it.identifier().getPath().equals("client"))
                .map(schema -> mapConfig(schema, Balm.getConfig().getLocalConfig(schema))).collect(Collectors.toSet());
        configsByType.put(ConfigType.UNIVERSAL, universalConfigs);
        configsByType.put(ConfigType.CLIENT, clientConfigs);
        return ConfigScreenHelper.createSelectionScreen(parent,
                Component.translatable(ConfigLocalization.forTitle(modId)),
                configsByType,
                new ResourceLocation("textures/block/stone.png")
        );
    }

    @Override
    public Set<IModConfig> getConfigurationsForMod(ModContext modContext) {
        final var configs = Balm.getConfig().getSchemasByNamespace(modContext.modId());
        return configs.stream()
                .map(schema -> mapConfig(schema, Balm.getConfig().getLocalConfig(schema)))
                .collect(Collectors.toSet());
    }
}
