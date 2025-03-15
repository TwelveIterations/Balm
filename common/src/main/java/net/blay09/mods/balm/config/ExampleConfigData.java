package net.blay09.mods.balm.config;

import net.blay09.mods.balm.api.config.v2.reflection.*;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Config("balm")
public class ExampleConfigData implements BalmConfigHolder {

    public static final String EXAMPLE_STATIC = "test";

    public enum ExampleEnum {
        Hello,
        World
    }

    @Comment("This is an example boolean property")
    public boolean exampleBoolean = true;
    @Comment("This is an example int property")
    public int exampleInt = 42;
    @Comment("This is an example string property")
    public String exampleString = "Hello World";
    @Comment("This is an example multiline string property")
    public String exampleMultilineString = "Hello World";
    @Comment("This is an example enum property")
    public ExampleEnum exampleEnum = ExampleEnum.Hello;
    @Synced
    @NestedType(String.class)
    @Comment("This is an example string list property")
    public List<String> exampleStringList = Arrays.asList("Hello", "World");
    @Synced
    @NestedType(ResourceLocation.class)
    @Comment("This is an example resource location set property")
    public Set<ResourceLocation> exampleResourceLocationSet = Set.of(ResourceLocation.withDefaultNamespace("dirt"), ResourceLocation.withDefaultNamespace("diamond"));
    @NestedType(Integer.class)
    @Comment("This is an example int list property")
    public List<Integer> exampleIntList = Arrays.asList(12, 24);
    @NestedType(ExampleEnum.class)
    @Comment("This is an example enum list property")
    public List<ExampleEnum> exampleEnumList = Arrays.asList(ExampleEnum.Hello, ExampleEnum.World);

    @Comment("This is an example category")
    public ExampleCategory exampleCategory = new ExampleCategory();

    public static class ExampleCategory {
        @Comment("This is an example string inside a category")
        public String innerField = "I am inside";
        @Comment("This is an example float inside a category")
        public float exampleFloat = 42.84f;
    }
}
