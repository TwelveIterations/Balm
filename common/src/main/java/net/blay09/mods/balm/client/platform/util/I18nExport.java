package net.blay09.mods.balm.client.platform.util;

import com.google.gson.Gson;
import net.blay09.mods.balm.Balm;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class I18nExport {

    public static Set<String> collectStaticI18nKeys(String namespace) {
        final Map<ResourceKey<? extends Registry<?>>, Function<Identifier, String>> registryToI18nMappers = new HashMap<>();
        registryToI18nMappers.put(Registries.CREATIVE_MODE_TAB, it -> it.toLanguageKey("itemGroup"));
        registryToI18nMappers.put(Registries.ITEM, it -> it.toLanguageKey("item"));

        final var result = new HashSet<String>();
        final var options = Minecraft.getInstance().options;
        for (final var keyMapping : options.keyMappings) {
            if (keyMapping.getName().startsWith("key." + namespace + ".")) {
                result.add(keyMapping.getName());
            }
            final var keyCategoryId = keyMapping.getCategory().id();
            if (keyCategoryId.getNamespace().equals(namespace)) {
                result.add(keyCategoryId.toLanguageKey("key.category"));
            }
        }

        for (final var schema : Balm.config().getSchemasByNamespace(namespace)) {
            result.add(schema.identifier().getNamespace() + ".configuration.title");
            for (final var property : schema.rootProperties()) {
                result.add(schema.identifier().getNamespace() + ".configuration." + property.name());
                result.add(schema.identifier().getNamespace() + ".configuration." + property.name() + ".tooltip");
            }
            for (final var category : schema.categories()) {
                result.add(schema.identifier().getNamespace() + ".configuration." + category.name());
                for (final var property : category.properties()) {
                    result.add(schema.identifier().getNamespace() + ".configuration." + category.name() + "." + property.name());
                    result.add(schema.identifier().getNamespace() + ".configuration." + category.name() + "." + property.name() + ".tooltip");
                }
            }
        }

        BuiltInRegistries.REGISTRY.stream().forEach(registry -> {
            for (final var identifier : registry.keySet()) {
                final var mapper = registryToI18nMappers.get(registry.key());
                if (mapper != null && identifier.getNamespace().equals(namespace)) {
                    result.add(mapper.apply(identifier));
                }
            }
        });

        return result;
    }

    public static void writeStaticI18nKeys(String namespace, File file) {
        try (final var fileWriter = new FileWriter(file)) {
            new Gson().toJson(collectStaticI18nKeys(namespace), fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
