package net.blay09.mods.balm.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.config.reflection.Config} instead.
 */
@Deprecated(forRemoval = true, since = "1.21.5")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String value();
}
