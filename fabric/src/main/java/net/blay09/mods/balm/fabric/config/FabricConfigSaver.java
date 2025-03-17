package net.blay09.mods.balm.fabric.config;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.notoml.Notoml;
import net.blay09.mods.balm.notoml.NotomlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FabricConfigSaver {

    public static Notoml toNotoml(BalmConfigSchema schema, LoadedConfig config) {
        Table<String, String, Object> properties = HashBasedTable.create();
        Table<String, String, String> comments = HashBasedTable.create();
        for (final var rootProperty : schema.rootProperties()) {
            final var value = config.getRaw(rootProperty);
            properties.put("", rootProperty.name(), value);
            comments.put("", rootProperty.name(), rootProperty.comment());
        }
        for (final var category : schema.categories()) {
            for (final var property : category.properties()) {
                final var value = config.getRaw(property);
                properties.put(property.category(), property.name(), value);
                comments.put(property.category(), property.name(), property.comment());
            }
        }
        return new Notoml(properties, comments);
    }

    public static void save(File configFile, BalmConfigSchema schema, LoadedConfig config) throws IOException {
        var notoml = toNotoml(schema, config);
        try (FileWriter writer = new FileWriter(configFile)) {
            NotomlSerializer.serialize(writer, notoml);
        }
    }
}
