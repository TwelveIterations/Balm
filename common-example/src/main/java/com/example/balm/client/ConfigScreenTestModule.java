package com.example.balm.client;

import com.example.balm.ExampleConfig;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenRegistrar;
import net.blay09.mods.balm.client.platform.config.BalmCustomConfigControlRegistrar;
import net.blay09.mods.balm.client.platform.config.ConfigControl;
import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public class ConfigScreenTestModule implements BalmClientModule {
    @Override
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath("balm_example", "config_screen");
    }

    @Override
    public void registerCustomConfigControls(BalmCustomConfigControlRegistrar customConfigControls) {
        customConfigControls.register("fancy_button", ConfigControl.<Boolean>builder()
                .element(FancyConfigButton::new)
                .build());
    }

    @Override
    public void registerConfigScreen(BalmConfigScreenRegistrar configScreens) {
        configScreens.register(ConfigScreenTestModule::createConfigScreen);
    }

    private static Screen createConfigScreen(Screen parent) {
        final var schema = Objects.requireNonNull(Balm.config().getSchema(ExampleConfig.class), "Example config schema not registered");
        final var rangedValue = rootProperty(schema, "rangedValue");
        final var fancyBoolean = rootProperty(schema, "fancyBoolean");
        final var welcomeMessage = rootProperty(schema, "welcomeMessage");
        final var targetBlock = rootProperty(schema, "targetBlock");
        final var spawnMode = rootProperty(schema, "spawnMode");
        final var favoriteItems = rootProperty(schema, "favoriteItems");
        final var luckyNumbers = rootProperty(schema, "luckyNumbers");
        final var experimentalEnabled = categoryProperty(schema, "experimental", "enabled");
        final var experimentalChance = categoryProperty(schema, "experimental", "chance");
        final var experimentalMaxPower = categoryProperty(schema, "experimental", "maxPower");

        return BalmConfigScreen.builder()
                .title(Component.translatable("balm_example.configuration.custom.title"))
                .section(Component.translatable("balm_example.configuration.section.general"), section -> section
                        .properties(rangedValue, fancyBoolean, welcomeMessage, targetBlock))
                .section(Component.translatable("balm_example.configuration.section.spawning"), section -> section
                        .property(spawnMode)
                        .property(experimentalEnabled)
                        .property(experimentalChance, context -> isEnabled(context, experimentalEnabled))
                        .property(experimentalMaxPower, context -> isEnabled(context, experimentalEnabled)))
                .section(Component.translatable("balm_example.configuration.section.collections"), section -> section
                        .properties(favoriteItems, luckyNumbers))
                .build(parent);
    }

    @SuppressWarnings("unchecked")
    private static boolean isEnabled(BalmConfigScreenContext context, ConfiguredProperty<?> property) {
        return context.bindingFor((ConfiguredProperty<Boolean>) property).get();
    }

    private static ConfiguredProperty<?> rootProperty(BalmConfigSchema schema, String name) {
        return Objects.requireNonNull(schema.findRootProperty(name), "Missing root config property: " + name);
    }

    private static ConfiguredProperty<?> categoryProperty(BalmConfigSchema schema, String category, String name) {
        return Objects.requireNonNull(schema.findProperty(category, name), "Missing config property: " + category + "." + name);
    }

    private static class FancyConfigButton extends Button.Plain {
        private final ConfigControlBinding<Boolean> binding;

        public FancyConfigButton(ConfigControlBinding<Boolean> binding, ConfigControlContext context) {
            super(0, 0, context.entryWidth(), context.entryHeight(), Component.empty(), (_) -> binding.set(!binding.get()), DEFAULT_NARRATION);
            this.binding = binding;
        }

        @Override
        public Component getMessage() {
            return Component.literal("Custom: " + binding.get());
        }
    }
}
