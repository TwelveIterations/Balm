package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.config.schema.builder.BalmConfigCategoryInitializer;
import net.blay09.mods.balm.api.config.schema.impl.ConfigSchemaImpl;
import net.blay09.mods.balm.api.config.schema.*;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategoryBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Set;

public class NewExampleConfig {
    public static final String EXAMPLE_STATIC = "test";

    public enum ExampleEnum implements StringRepresentable {
        Hello,
        World;

        @Override
        public String getSerializedName() {
            return name();
        }
    }

    public static final ConfigSchemaImpl schema = BalmConfigSchema.create(ResourceLocation.fromNamespaceAndPath("balm", "common"));
    public static final ConfiguredBoolean exampleBoolean = schema
            .property("exampleBoolean")
            .comment("This is an example boolean property")
            .boolOf(true);

    public static final ConfiguredInt exampleInt = schema
            .property("exampleInt")
            .comment("This is an example int property")
            .intOf(42);

    public static final ConfiguredString exampleString = schema
            .property("exampleString")
            .comment("This is an example string property")
            .stringOf("Hello World");

    public static final ConfiguredEnum<ExampleEnum> exampleEnum = schema
            .property("exampleEnum")
            .comment("This is an example enum property")
            .enumOf(ExampleEnum.Hello);

    public static final ConfiguredList<String> exampleStringList = schema
            .property("exampleStringList")
            .comment("This is an example string list property")
            .synced()
            .listOf(String.class, List.of("Hello", "World"));

    public static final ConfiguredSet<ResourceLocation> exampleResourceLocationSet = schema
            .property("exampleResourceLocationSet")
            .comment("This is an example resource location set property")
            .synced()
            .setOf(ResourceLocation.class, Set.of(ResourceLocation.withDefaultNamespace("dirt"), ResourceLocation.withDefaultNamespace("diamond")));

    public static final ConfiguredList<Integer> exampleIntList = schema
            .property("exampleIntList")
            .comment("This is an example int list property")
            .listOf(Integer.class, List.of(12, 24));

    public static final ConfiguredList<ExampleEnum> exampleEnumList = schema
            .property("exampleEnumList")
            .comment("This is an example enum list property")
            .listOf(ExampleEnum.class, List.of(ExampleEnum.Hello, ExampleEnum.World));

    public static final ExampleCategory exampleCategory = schema.category("exampleCategory")
            .comment("This is an example category")
            .via(ExampleCategory::new);

    public static class ExampleCategory extends BalmConfigCategoryInitializer {
        public final ConfiguredString innerField = category
                .property("innerField")
                .comment("This is an example string inside a category")
                .stringOf("I am inside");

        public final ConfiguredFloat exampleFloat = category
                .property("exampleFloat")
                .comment("This is an example float inside a category")
                .floatOf(42.84f);

        public ExampleCategory(ConfigCategoryBuilder category) {
            super(category);
        }
    }

    public NewExampleConfig() {
        // Balm.getConfig().registerConfig(id("common"), ConfigReflection.schemaOf(ExampleConfigData.class))
        // Balm.getConfig().registerConfig(ExampleConfigData.class);
        // Balm.getConfig().getLocalConfig(id("common"))
        // Balm.getConfig().getLocalConfig(ExampleConfigData.class)
        // Balm.getConfig().getActiveConfig(id("common"))
        // Balm.getConfig().getActiveConfig(ExampleConfigData.class)
        // Balm.getConfig().getConfigDir()
        // Balm.getConfig().getConfigDir()

        // final var filled = ConfigReflection.applyFrom(new ExampleConfigData(), inferredSchema);
        // final var loaded = schema.loadFrom(notoml.toTable());
        // final var synced = schema.loadFrom(message.toTable());
        // final var defaults = schema.defaults();
        // final var instance = ConfigReflection.applyTo(defaults, new ExampleConfigData());
        // final var table = loaded.toTable();
    }
}
