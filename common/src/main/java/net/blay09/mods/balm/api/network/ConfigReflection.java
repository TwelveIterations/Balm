package net.blay09.mods.balm.api.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.reflection.*;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.builder.ConfigPropertyBuilder;
import net.blay09.mods.balm.api.config.schema.builder.PropertyHolderBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

public class ConfigReflection {

    public static BalmConfigSchema schemaOf(Class<?> configDataClass) {
        final var rootFields = getAllFields(configDataClass);
        final var rootDataFields = rootFields.stream().filter(it -> !ConfigReflection.isCategoryField(it)).toList();
        final var identifier = getIdentifier(configDataClass);
        final var schema = BalmConfigSchema.create(identifier);
        buildFieldsIntoSchema(schema, configDataClass, rootDataFields);
        final var categoryFields = rootFields.stream().filter(ConfigReflection::isCategoryField).toList();
        for (final var categoryField : categoryFields) {
            final var fields = getAllFields(categoryField.getType());
            final var category = schema.category(categoryField.getName());
            final var commentAnnotation = categoryField.getAnnotation(Comment.class);
            if (commentAnnotation != null) {
                category.comment(commentAnnotation.value());
            }
            buildFieldsIntoSchema(category, categoryField.getType(), fields);
        }
        return schema;
    }

    @SuppressWarnings("unchecked")
    private static void buildFieldsIntoSchema(PropertyHolderBuilder builder, Class<?> clazz, List<Field> fields) {
        final var defaults = createInstance(clazz);
        for (final var field : fields) {
            final var property = builder.property(field.getName());
            final var commentAnnotation = field.getAnnotation(Comment.class);
            if (commentAnnotation != null) {
                property.comment(commentAnnotation.value());
            }
            if (field.getAnnotation(Synced.class) != null) {
                property.synced();
            }
            final var type = field.getType();
            final var nestedTypeAnnotation = field.getAnnotation(NestedType.class);
            final var nestedType = nestedTypeAnnotation != null ? nestedTypeAnnotation.value() : null;
            try {
                final var defaultValue = field.get(defaults);
                if (type == String.class) {
                    property.stringOf((String) defaultValue);
                } else if (type == ResourceLocation.class) {
                    property.resourceLocationOf((ResourceLocation) defaultValue);
                } else if (type == Integer.class || type == int.class) {
                    property.intOf((int) defaultValue);
                } else if (type == Long.class || type == long.class) {
                    property.longOf((long) defaultValue);
                } else if (type == Float.class || type == float.class) {
                    property.floatOf((float) defaultValue);
                } else if (type == Double.class || type == double.class) {
                    property.doubleOf((double) defaultValue);
                } else if (type == Boolean.class || type == boolean.class) {
                    property.boolOf((boolean) defaultValue);
                } else if (type.isEnum()) {
                    propertyOfEnum(property, defaultValue);
                } else if (List.class.isAssignableFrom(type)) {
                    if (nestedType != null) {
                        @SuppressWarnings("rawtypes")
                        List listValue = (List) defaultValue;
                        property.listOf(nestedType, listValue);
                    } else {
                        throw new IllegalArgumentException("List field " + field.getName() + " in class " + clazz.getName() + " is missing @NestedType annotation");
                    }
                } else if (Set.class.isAssignableFrom(type)) {
                    if (nestedType != null) {
                        @SuppressWarnings("rawtypes")
                        Set setValue = (Set) defaultValue;
                        property.setOf(nestedType, setValue);
                    } else {
                        throw new IllegalArgumentException("Set field " + field.getName() + " in class " + clazz.getName() + " is missing @NestedType annotation");
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported config field type " + type.getName() + " in class " + clazz.getName());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing config field " + field.getName() + " in class " + clazz.getName(), e);
            }
        }
    }

    private static <T extends Enum<T> & StringRepresentable> void propertyOfEnum(ConfigPropertyBuilder property, Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        if (!(obj instanceof Enum)) {
            throw new IllegalArgumentException("Object must be an Enum");
        }

        if (!(obj instanceof StringRepresentable)) {
            throw new IllegalArgumentException("Object must implement StringRepresentable");
        }

        @SuppressWarnings("unchecked")
        T enumValue = (T) obj;

        property.enumOf(enumValue);
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Config class " + clazz.getName() + " must have a public no-arg constructor.", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error instantiating config class " + clazz.getName(), e);
        }
    }

    private static boolean isConfigDataField(Field field) {
        return !Modifier.isFinal(field.getModifiers())
                && !Modifier.isStatic(field.getModifiers())
                && field.getAnnotation(IgnoreConfig.class) == null;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getFields()).filter(ConfigReflection::isConfigDataField).toList();
    }

    private static boolean isCategoryField(Field field) {
        return !field.getType().isPrimitive() && !field.getType()
                .isEnum() && field.getType() != String.class && field.getType() != List.class && field.getType() != Set.class && field.getType() != ResourceLocation.class;
    }

    public static ResourceLocation getIdentifier(Class<?> configDataClass) {
        final var configAnnotation = configDataClass.getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("Class " + configDataClass.getName() + " is missing a @Config annotation");
        }
        return ResourceLocation.fromNamespaceAndPath(configAnnotation.value(), configAnnotation.type());
    }

    public static <T> LoadedReflectionConfig<T> of(Class<T> configDataClass, LoadedConfig loadedConfig) {
        final var instance = createInstance(configDataClass);
        final var schema = Balm.getConfig().getSchema(configDataClass);
        final var config = new LoadedReflectionConfig<>(instance);
        config.applyFrom(schema, loadedConfig);
        return config;
    }
}
