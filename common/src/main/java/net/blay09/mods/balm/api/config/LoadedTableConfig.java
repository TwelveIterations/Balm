package net.blay09.mods.balm.api.config;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

import java.util.ArrayList;
import java.util.List;

public record LoadedTableConfig(Table<String, String, Object> table) implements MutableLoadedConfig {

    public LoadedTableConfig() {
        this(HashBasedTable.create());
    }

    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        if (property.type().isAssignableFrom(value.getClass())) {
            table.put(property.category(), property.name(), value);
        } else {
            throw new IllegalArgumentException("Invalid type for property " + property.name() + " in category " + property.category() + ": " + value.getClass()
                    .getName() + ", expected " + property.type().getName());
        }
    }

    @Override
    public MutableLoadedConfig copy() {
        return new LoadedTableConfig(HashBasedTable.create(table));
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getRaw(ConfiguredProperty<T> property) {
        final var value = table.get(property.category(), property.name());
        if (value == null) {
            return property.defaultValue();
        }
        if (!property.type().isAssignableFrom(value.getClass())) {
            return property.defaultValue();
        }
        return (T) value;
    }

    public static Pair<LoadedTableConfig, List<Throwable>> of(BalmConfigSchema schema, Table<String, String, Object> table) {
        final var validatedTable = HashBasedTable.<String, String, Object>create();
        final var errors = new ArrayList<Throwable>();
        for (final var rootProperty : schema.rootProperties()) {
            try {
                final var value = validate(rootProperty, table);
                validatedTable.put(rootProperty.category(), rootProperty.name(), value);
            } catch (Throwable e) {
                validatedTable.put(rootProperty.category(), rootProperty.name(), rootProperty.defaultValue());
                errors.add(e);
            }
        }
        for (final var category : schema.categories()) {
            for (final var property : category.properties()) {
                try {
                    final var value = validate(property, table);
                    validatedTable.put(property.category(), property.name(), value);
                } catch (Throwable e) {
                    validatedTable.put(property.category(), property.name(), property.defaultValue());
                    errors.add(e);
                }
            }
        }
        return Pair.of(new LoadedTableConfig(table), errors);
    }

    private static <T> T validate(ConfiguredProperty<T> property, Table<String, String, Object> table) {
        final var value = table.get(property.category(), property.name());
        return property.codec().decode(JavaOps.INSTANCE, value).getOrThrow().getFirst();
    }
}
