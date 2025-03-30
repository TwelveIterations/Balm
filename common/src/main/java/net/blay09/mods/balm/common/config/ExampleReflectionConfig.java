package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.config.reflection.Comment;
import net.blay09.mods.balm.api.config.reflection.Config;
import net.blay09.mods.balm.api.config.reflection.NestedType;
import net.blay09.mods.balm.api.config.reflection.Synced;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Config("balm")
public class ExampleReflectionConfig {

    public static final String EXAMPLE_STATIC = "test";
    @Comment("This is an example boolean property")
    public boolean exampleBoolean = true;
    @Comment("This is an example int property")
    public int exampleInt = 42;
    @Comment("This is an example string property")
    public String exampleString = "Hello World";
    @Comment("This is an example enum property")
    public ExampleEnum exampleEnum = ExampleEnum.Hello;
    @Synced
    @NestedType(String.class)
    @Comment("This is an example string list property")
    public List<String> exampleStringList = Arrays.asList("Hello", "World");
    @Synced
    @NestedType(ResourceLocation.class)
    @Comment("This is an example resource location set property")
    public Set<ResourceLocation> exampleResourceLocationSet = Set.of(new ResourceLocation("dirt"),
            new ResourceLocation("diamond"));
    @NestedType(Integer.class)
    @Comment("This is an example int list property")
    public List<Integer> exampleIntList = Arrays.asList(12, 24);
    @NestedType(ExampleEnum.class)
    @Comment("This is an example enum list property")
    public List<ExampleEnum> exampleEnumList = Arrays.asList(ExampleEnum.Hello, ExampleEnum.World);
    @Comment("This is an example category")
    public ExampleCategory exampleCategory = new ExampleCategory();

    public enum ExampleEnum implements StringRepresentable {
        Hello,
        World;

        @Override
        public String getSerializedName() {
            return name();
        }
    }

    public static class ExampleCategory {
        @Comment("This is an example string inside a category")
        public String innerField = "I am inside";

        @Comment("This is an example float inside a category")
        @Synced
        public float exampleFloat = 42.84f;
    }
}
