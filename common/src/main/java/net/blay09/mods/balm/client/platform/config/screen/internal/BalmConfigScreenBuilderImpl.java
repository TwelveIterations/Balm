package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenBuilder;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenSectionBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BalmConfigScreenBuilderImpl implements BalmConfigScreenBuilder {
    private final List<BalmConfigScreenSection> sections = new ArrayList<>();
    private Component title = Component.empty();

    @Override
    public BalmConfigScreenBuilderImpl title(Component title) {
        this.title = title;
        return this;
    }

    @Override
    public BalmConfigScreenBuilderImpl section(Component title, Consumer<BalmConfigScreenSectionBuilder> initializer) {
        final var sectionBuilder = new BalmConfigScreenSectionBuilderImpl();
        initializer.accept(sectionBuilder);
        sections.add(new BalmConfigScreenSection(title, List.copyOf(sectionBuilder.rows())));
        return this;
    }

    @Override
    public BalmConfigScreen build(@Nullable Screen parent) {
        return new BalmConfigScreen(parent, title, List.copyOf(sections));
    }
}
