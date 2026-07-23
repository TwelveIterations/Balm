package com.example.balm;

import net.blay09.mods.balm.platform.config.reflection.CustomControl;
import net.blay09.mods.balm.platform.config.reflection.Comment;
import net.blay09.mods.balm.platform.config.reflection.Config;
import net.blay09.mods.balm.platform.config.reflection.NestedType;
import net.blay09.mods.balm.platform.config.reflection.Range;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Set;

@Config("balm_example")
public class ExampleConfig {
    @Comment("Example ranged integer config value.")
    @Range(min = "0", max = "10")
    public int rangedValue = 5;

    @Comment("Example custom widget config value.")
    @CustomControl("fancy_button")
    public boolean fancyBoolean;

    @Comment("Example string config value.")
    public String welcomeMessage = "Hello from Balm!";

    @Comment("Example identifier config value.")
    public Identifier targetBlock = Identifier.withDefaultNamespace("diamond_block");

    @Comment("Example enum config value.")
    public SpawnMode spawnMode = SpawnMode.NEAR_PLAYER;

    @Comment("Example list config value.")
    @NestedType(String.class)
    public List<String> favoriteItems = List.of("minecraft:apple", "minecraft:bread");

    @Comment("Example set config value.")
    @NestedType(Integer.class)
    public Set<Integer> luckyNumbers = Set.of(3, 7, 9);

    @Comment("Example category for experimental settings.")
    public Experimental experimental = new Experimental();

    @Comment("Example category shown as a child config screen.")
    public ChildScreen childScreen = new ChildScreen();

    public enum SpawnMode implements StringRepresentable {
        NEAR_PLAYER("near_player"),
        WORLD_SPAWN("world_spawn"),
        DISABLED("disabled");

        private final String serializedName;

        SpawnMode(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }
    }

    public static class Experimental {
        @Comment("Show advanced experimental options in the custom config screen.")
        public boolean enabled;

        @Comment("Example floating point config value.")
        @Range(min = "0", max = "1")
        public double chance = 0.25;

        @Comment("Example long config value.")
        @Range(min = "0", max = "1000000")
        public long maxPower = 9001;
    }

    public static class ChildScreen {
        @Comment("Example child screen toggle.")
        public boolean enabled = true;

        @Comment("Example child screen message.")
        public String message = "Configured from a child screen";

        @Comment("Example child screen number.")
        public int number = 987;
    }
}
