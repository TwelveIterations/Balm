package net.blay09.mods.balm.api.network;

import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.reflection.Comment;
import net.blay09.mods.balm.api.config.v2.reflection.Config;
import net.blay09.mods.balm.api.config.v2.reflection.IgnoreConfig;
import net.blay09.mods.balm.api.config.v2.reflection.Synced;
import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.v2.schema.builder.PropertyHolderBuilder;
import net.blay09.mods.balm.api.config.v2.schema.impl.ConfigSchemaImpl;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ConfigReflection {

    public static BalmConfigSchema schemaOf(Class<?> configDataClass) {
        final var rootFields = getAllFields(configDataClass);
        final var rootDataFields = rootFields.stream().filter(it -> !ConfigReflection.isCategoryField(it)).toList();
        final var identifier = getIdentifier(configDataClass);
        final var schema = BalmConfigSchema.create(identifier);
        buildFieldsIntoSchema(schema, rootDataFields);
        final var categoryFields = rootFields.stream().filter(ConfigReflection::isCategoryField).toList();
        for (final var categoryField : categoryFields) {
            final var fields = getAllFields(categoryField.getClass());
            final var category = schema.category(categoryField.getName());
            final var commentAnnotation = categoryField.getAnnotation(Comment.class);
            if (commentAnnotation != null) {
                category.comment(commentAnnotation.value());
            }
            buildFieldsIntoSchema(category, fields);
        }
        return schema;
    }

    private static void buildFieldsIntoSchema(PropertyHolderBuilder builder, List<Field> fields) {
        for (final var field : fields) {
            final var property = builder.property(field.getName());
            final var commentAnnotation = field.getAnnotation(Comment.class);
            if (commentAnnotation != null) {
                property.comment(commentAnnotation.value());
            }
            if (field.getAnnotation(Synced.class) != null) {
                property.synced();
            }
            // TODO
        }
    }

    public static boolean isConfigDataField(Field field) {
        return !Modifier.isFinal(field.getModifiers())
                && !Modifier.isStatic(field.getModifiers())
                && field.getAnnotation(IgnoreConfig.class) == null;
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getFields()).filter(ConfigReflection::isConfigDataField).toList();
    }

    public static List<Field> getSyncedFields(Class<?> clazz) {
        return getAllFields(clazz).stream().filter(ConfigReflection::isSyncedFieldOrCategory).toList();
    }

    public static boolean isSyncedFieldOrCategory(Field field) {
        boolean hasSyncedAnnotation = field.getAnnotation(Synced.class) != null;
        return hasSyncedAnnotation || isCategoryField(field);
    }

    public static boolean isCategoryField(Field field) {
        return !field.getType().isPrimitive() && !field.getType()
                .isEnum() && field.getType() != String.class && field.getType() != List.class && field.getType() != Set.class && field.getType() != ResourceLocation.class;
    }

    public static Object deepCopy(Object from, Object to) {
        Field[] fields = from.getClass().getFields();
        for (Field field : fields) {
            if (!isConfigDataField(field)) {
                continue;
            }

            Class<?> type = field.getType();
            try {
                if (String.class.isAssignableFrom(type) || ResourceLocation.class.isAssignableFrom(type) || Enum.class.isAssignableFrom(type) || type.isPrimitive()) {
                    field.set(to, field.get(from));
                } else if (List.class.isAssignableFrom(type)) {
                    field.set(to, new ArrayList((Collection) field.get(from)));
                } else if (Set.class.isAssignableFrom(type)) {
                    field.set(to, new HashSet(((Collection) field.get(from))));
                } else {
                    field.set(to, deepCopy(field.get(from), field.get(to)));
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return to;
    }

    public static ResourceLocation getIdentifier(Class<?> configDataClass) {
        final var configAnnotation = configDataClass.getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("Class " + configDataClass.getName() + " is missing a @Config annotation");
        }
        return ResourceLocation.fromNamespaceAndPath(configAnnotation.value(), "common");
    }

    public static <T> T of(Class<T> configDataClass, LoadedConfig loadedConfig) {
        return null;
    }
}
