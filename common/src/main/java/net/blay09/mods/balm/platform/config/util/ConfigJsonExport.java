package net.blay09.mods.balm.platform.config.util;

import com.google.gson.Gson;
import net.blay09.mods.balm.platform.config.schema.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * This is used internally to generate a JSON file containing all config properties and their metadata for use in the documentation website.
 */
public class ConfigJsonExport {

    public static ExportedConfig mapToExportData(Collection<BalmConfigSchema> schemas) {
        final var properties = new ArrayList<ConfigProperty>();
        for (final var schema : schemas) {
            for (final var property : schema.rootProperties()) {
                properties.add(new ConfigProperty(property));
            }
            for (final var category : schema.categories()) {
                for (final var property : category.properties()) {
                    properties.add(new ConfigProperty(property));
                }
            }
        }
        return new ExportedConfig(properties);
    }

    public static void exportToFile(Collection<BalmConfigSchema> schemas, File file) throws IOException {
        final var parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException("Failed to create parent directories for file: " + file);
        }
        Files.writeString(file.toPath(), new Gson().toJson(mapToExportData(schemas)));
    }

    @Nullable
    private static String[] getValidValues(ConfiguredProperty<?> property) {
        Class<?> enumType = null;
        if (property instanceof ConfiguredEnum<?> enumProperty) {
            enumType = enumProperty.type();
        } else if (property instanceof ConfiguredList<?> listProperty && listProperty.nestedType().isEnum()) {
            enumType = listProperty.nestedType();
        } else if (property instanceof ConfiguredSet<?> setProperty && setProperty.nestedType().isEnum()) {
            enumType = setProperty.nestedType();
        }

        if (enumType != null) {
            return Arrays.stream(enumType.getEnumConstants())
                    .map(Object::toString)
                    .toArray(String[]::new);
        }

        return null;
    }

    public record ExportedConfig(List<ConfigProperty> properties) {}

    public record ConfigProperty(String configType, String category, String name, String type, String description, String defaultValue,
                                 @Nullable String[] validValues) {

        public ConfigProperty(ConfiguredProperty<?> property) {
            this(property.parentSchema().identifier().getPath(), property.category(),
                    property.name(),
                    property.type().getSimpleName(),
                    property.comment(),
                    Objects.toString(property.defaultValue()),
                    getValidValues(property));
        }
    }
}
