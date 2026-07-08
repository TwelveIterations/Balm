package net.blay09.mods.balm.neoforge.client.platform.config.internal;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlRegistry;
import net.blay09.mods.balm.neoforge.platform.config.internal.NeoForgeBalmConfig;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfigControlContext;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BalmNeoForgeConfigurationScreen extends ConfigurationScreen.ConfigurationSectionScreen {
    private static final String SECTION = "neoforge.configuration.uitext.section";
    private static final String SECTION_TEXT = "neoforge.configuration.uitext.sectiontext";

    private final @Nullable BalmConfigSchema schema;

    public BalmNeoForgeConfigurationScreen(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title) {
        super(parent, type, modConfig, title);
        schema = Balm.config().getSchema(Identifier.fromNamespaceAndPath(modConfig.getModId(), modConfig.getType().extension()));
    }

    private BalmNeoForgeConfigurationScreen(Context parentContext, Screen parent, @Nullable BalmConfigSchema schema, UnmodifiableConfig valueSpecs, String key, UnmodifiableConfig subsection, Component title) {
        super(parentContext, parent, valueSpecs.valueMap(), key, subsection.entrySet(), title);
        this.schema = schema;
    }

    @Nullable
    private ConfiguredProperty<?> findProperty(String key) {
        if (schema == null) {
            return null;
        }

        final var path = context.keylist();
        if (path.isEmpty()) {
            return schema.rootProperties().stream().filter(it -> it.name().equals(key)).findFirst().orElse(null);
        } else if (path.size() == 1) {
            final var categoryName = path.getFirst();
            return schema.categories().stream()
                    .filter(it -> it.name().equals(categoryName))
                    .flatMap(it -> it.properties().stream())
                    .filter(it -> it.name().equals(key))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <R, T> Element createCustomElement(String key, Supplier<R> source, Consumer<R> target) {
        final var property = (ConfiguredProperty<T>) findProperty(key);
        if (property == null) {
            return null;
        }

        final var customControlId = property.customControl().orElse(null);
        if (customControlId == null) {
            return null;
        }

        final Supplier<T> getter = () -> (T) NeoForgeBalmConfig.mapConfigValueFromNeoForge(property, source.get());
        final Consumer<T> setter = value -> {
            target.accept((R) NeoForgeBalmConfig.mapConfigValueToNeoForge(value));
            onChanged(key);
        };
        final var context = new ConfigControlContext<>(property, getter, setter, getTranslationComponent(key), getTooltipComponent(key, null));
        final var element = ConfigControlRegistry.createElement(customControlId, context).orElse(null);
        return switch (element) {
            case null -> null;
            case Element neoForgeElement -> neoForgeElement;
            case AbstractWidget widget ->
                    new Element(getTranslationComponent(key), getTooltipComponent(key, null), widget);
            case OptionInstance<?> option ->
                    new Element(getTranslationComponent(key), getTooltipComponent(key, null), option);
            default ->
                    throw new IllegalStateException("Config control for " + property.parentSchema().identifier() + "/" + property.category() + "." + property.name() + " must return ConfigurationSectionScreen.Element, AbstractWidget, or OptionInstance for NeoForge, got " + element.getClass().getName());
        };

    }

    @Override
    protected @Nullable Element createStringValue(String key, java.util.function.Predicate<String> tester, Supplier<String> source, Consumer<String> target) {
        final var element = createCustomElement(key, source, target);
        return element != null ? element : super.createStringValue(key, tester, source, target);
    }

    @Override
    protected @Nullable Element createBooleanValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Boolean> source, Consumer<Boolean> target) {
        final var element = createCustomElement(key, source, target);
        return element != null ? element : super.createBooleanValue(key, spec, source, target);
    }

    @Override
    protected <T extends Enum<T>> @Nullable Element createEnumValue(String key, ModConfigSpec.ValueSpec spec, Supplier<T> source, Consumer<T> target) {
        final var element = createCustomElement(key, source, target);
        return element != null ? element : super.createEnumValue(key, spec, source, target);
    }

    @Override
    protected @Nullable Element createIntegerValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Integer> source, Consumer<Integer> target) {
        final var element = createCustomElement(key, source, target);
        return element != null ? element : super.createIntegerValue(key, spec, source, target);
    }

    @Override
    protected @Nullable Element createLongValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Long> source, Consumer<Long> target) {
        final var element = createCustomElement(key, source, target);
        return element != null ? element : super.createLongValue(key, spec, source, target);
    }

    @Override
    protected @Nullable Element createDoubleValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Double> source, Consumer<Double> target) {
        final var element = createCustomElement(key, source, target);
        return element != null ? element : super.createDoubleValue(key, spec, source, target);
    }

    @Override
    protected <T> @Nullable Element createList(String key, ModConfigSpec.ListValueSpec spec, ModConfigSpec.ConfigValue<List<T>> list) {
        final var element = createCustomElement(key, list::getRaw, list::set);
        return element != null ? element : super.createList(key, spec, list);
    }

    @Override
    protected Element createSection(String key, UnmodifiableConfig valueSpecs, UnmodifiableConfig subsection) {
        return new Element(Component.translatable(SECTION, getTranslationComponent(key)), getTooltipComponent(key, null),
                Button.builder(Component.translatable(SECTION_TEXT),
                        _ -> minecraft.setScreen(sectionCache.computeIfAbsent(key,
                                _ -> new BalmNeoForgeConfigurationScreen(context, this, schema, valueSpecs, key, subsection, Component.translatable(getTranslationKey(key))).rebuild()))).build(), false);
    }
}
