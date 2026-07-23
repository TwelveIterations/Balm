package net.blay09.mods.balm.client.platform.config.screen;

import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public interface BalmConfigScreenContext {
    Font font();

    @Nullable Component getValidationError(ConfiguredProperty<?> property);

    void setValidationError(ConfiguredProperty<?> property, Component error);

    void clearValidationError(ConfiguredProperty<?> property);

    <T> ConfigControlBinding<T> bindingFor(ConfiguredProperty<T> property);

    BalmConfigScreenRowState stateFor(ConfiguredProperty<?> property);

    default <T> T valueFor(ConfiguredProperty<T> property) {
        return bindingFor(property).get();
    }
}
