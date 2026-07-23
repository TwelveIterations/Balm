package net.blay09.mods.balm.client.platform.config.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public interface BalmConfigScreenBuilder {
    BalmConfigScreenBuilder title(Component title);

    BalmConfigScreenBuilder section(Component title, Consumer<BalmConfigScreenSectionBuilder> initializer);

    default BalmConfigScreen build() {
        return build(null);
    }

    BalmConfigScreen build(@Nullable Screen parent);
}
