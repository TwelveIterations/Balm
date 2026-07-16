package com.example.balm;

import net.blay09.mods.balm.platform.config.reflection.CustomControl;
import net.blay09.mods.balm.platform.config.reflection.Comment;
import net.blay09.mods.balm.platform.config.reflection.Config;
import net.blay09.mods.balm.platform.config.reflection.Range;

@Config("balm_example")
public class ExampleConfig {
    @Comment("Example ranged integer config value.")
    @Range(min = "0", max = "10")
    public int rangedValue = 5;

    @Comment("Example custom widget config value.")
    @CustomControl("fancy_button")
    public boolean fancyBoolean;
}
