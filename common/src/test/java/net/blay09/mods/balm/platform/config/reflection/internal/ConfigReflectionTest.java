package net.blay09.mods.balm.platform.config.reflection.internal;

import net.blay09.mods.balm.platform.config.reflection.Config;
import net.blay09.mods.balm.platform.config.reflection.CustomControl;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigReflectionTest {

    @Test
    void resolvesCustomControlsUsingRootConfigNamespace() {
        final var schema = ConfigReflection.schemaOf(RootConfig.class);

        assertEquals(Identifier.parse("test:root_control"), schema.findRootProperty("enabled").customControl().orElseThrow());
        assertEquals(Identifier.parse("test:category_control"), schema.findProperty("category", "enabled").customControl().orElseThrow());
        assertEquals(Identifier.parse("other:explicit_control"), schema.findProperty("category", "explicit").customControl().orElseThrow());
    }

    @Test
    void stillRequiresConfigAnnotationOnRoot() {
        assertThrows(IllegalArgumentException.class, () -> ConfigReflection.schemaOf(Category.class));
    }

    @Config("test")
    public static class RootConfig {
        @CustomControl("root_control")
        public boolean enabled = true;
        public Category category = new Category();
    }

    public static class Category {
        @CustomControl("category_control")
        public boolean enabled = true;
        @CustomControl("other:explicit_control")
        public boolean explicit = true;
    }
}
