package net.blay09.mods.balm.client.platform.config;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.platform.config.schema.ConfigControlContext;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConfigControl<T> {
    private final ElementFactory<T> elementFactory;
    private final BiFunction<ConfigControlContext<T>, T, DataResult<T>> validator;
    private final Function<ConfigControlContext<T>, Optional<Component>> validationHint;

    private ConfigControl(ElementFactory<T> elementFactory, BiFunction<ConfigControlContext<T>, T, DataResult<T>> validator, Function<ConfigControlContext<T>, Optional<Component>> validationHint) {
        this.elementFactory = elementFactory;
        this.validator = validator;
        this.validationHint = validationHint;
    }

    private static <T> @Nullable Object noElement(ConfigControlContext<T> context) {
        return null;
    }

    public Optional<Object> createElement(ConfigControlContext<T> context) {
        return Optional.ofNullable(elementFactory.create(context));
    }

    public DataResult<T> validate(ConfigControlContext<T> context, T value) {
        return validator.apply(context, value);
    }

    public Optional<Component> getValidationHint(ConfigControlContext<T> context) {
        return validationHint.apply(context);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private ElementFactory<T> elementFactory = ConfigControl::noElement;
        private BiFunction<ConfigControlContext<T>, T, DataResult<T>> validator = (context, value) -> context.property().validateValue(value);
        private Function<ConfigControlContext<T>, Optional<Component>> validationHint = _ -> Optional.empty();

        public Builder<T> element(ElementFactory<T> elementFactory) {
            this.elementFactory = elementFactory;
            return this;
        }

        public Builder<T> validator(BiFunction<ConfigControlContext<T>, T, DataResult<T>> validator) {
            this.validator = validator;
            return this;
        }

        public Builder<T> validationHint(Component validationHint) {
            this.validationHint = context -> Optional.of(validationHint);
            return this;
        }

        public Builder<T> validationHint(Function<ConfigControlContext<T>, Optional<Component>> validationHint) {
            this.validationHint = validationHint;
            return this;
        }

        public ConfigControl<T> build() {
            return new ConfigControl<>(elementFactory, validator, validationHint);
        }
    }

    @FunctionalInterface
    public interface ElementFactory<T> {
        @Nullable
        Object create(ConfigControlContext<T> context);
    }
}
