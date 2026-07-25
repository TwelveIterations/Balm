package net.blay09.mods.balm.platform.config.reflection;

import net.blay09.mods.balm.platform.config.schema.ConfigValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateWith {
    Class<? extends ConfigValidator<?>> value();
}
