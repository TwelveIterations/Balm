package net.blay09.mods.balm.client.platform.config;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConfigControl<T> {
    private final ElementFactory<T> elementFactory;
    private final BiFunction<ConfigControlBinding<T>, T, DataResult<T>> validator;
    private final Function<ConfigControlBinding<T>, Optional<Component>> validationHint;

    private ConfigControl(ElementFactory<T> elementFactory, BiFunction<ConfigControlBinding<T>, T, DataResult<T>> validator, Function<ConfigControlBinding<T>, Optional<Component>> validationHint) {
        this.elementFactory = elementFactory;
        this.validator = validator;
        this.validationHint = validationHint;
    }

    private static <T> @Nullable Object noElement(ConfigControlBinding<T> context) {
        return null;
    }

    public Optional<Object> createElement(ConfigControlBinding<T> context) {
        return Optional.ofNullable(elementFactory.create(context));
    }

    public DataResult<T> validate(ConfigControlBinding<T> context, T value) {
        return validator.apply(context, value);
    }

    public Optional<Component> getValidationHint(ConfigControlBinding<T> context) {
        return validationHint.apply(context);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private ElementFactory<T> elementFactory = ConfigControl::noElement;
        private BiFunction<ConfigControlBinding<T>, T, DataResult<T>> validator = (context, value) -> context.validateValue(value);
        private Function<ConfigControlBinding<T>, Optional<Component>> validationHint = _ -> Optional.empty();

        public Builder<T> element(ElementFactory<T> elementFactory) {
            this.elementFactory = elementFactory;
            return this;
        }

        public Builder<T> validator(BiFunction<ConfigControlBinding<T>, T, DataResult<T>> validator) {
            this.validator = validator;
            return this;
        }

        public Builder<T> validationHint(Component validationHint) {
            this.validationHint = context -> Optional.of(validationHint);
            return this;
        }

        public Builder<T> validationHint(Function<ConfigControlBinding<T>, Optional<Component>> validationHint) {
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
        Object create(ConfigControlBinding<T> context);
    }
}
