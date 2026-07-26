package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.platform.config.schema.ConfigValidator;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.internal.ConfigSchemaImpl;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractConfigProperty<T> implements ConfiguredProperty<T> {
    private final ConfigSchemaImpl schema;
    private final String category;
    private final String name;
    private final String comment;
    private final boolean synced;
    private final @Nullable Identifier customControl;
    protected final @Nullable ConfigValidator<?> validator;

    public AbstractConfigProperty(ConfigPropertyBuilder parent) {
        schema = parent.schema;
        category = parent.category;
        name = parent.name;
        comment = parent.comment;
        synced = parent.synced;
        customControl = parent.customControl;
        validator = createValidator(parent.validatorClass);
    }

    @SuppressWarnings("unchecked")
    protected static <T> @Nullable ConfigValidator<T> createValidator(@Nullable Class<? extends ConfigValidator<?>> validatorClass) {
        if (validatorClass == null) {
            return null;
        }

        try {
            return (ConfigValidator<T>) validatorClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Config validator " + validatorClass.getName() + " must have a public no-arg constructor", e);
        }
    }

    @Override
    public ConfigSchemaImpl parentSchema() {
        return schema;
    }

    @Override
    public String category() {
        return category;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public boolean synced() {
        return synced;
    }

    @Override
    public Optional<Identifier> customControl() {
        return Optional.ofNullable(customControl);
    }

    @Override
    public boolean hasCustomValidator() {
        return validator != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataResult<T> validateValue(T value) {
        return validator != null ? ((ConfigValidator<T>) validator).validate(value) : DataResult.success(value);
    }
}
