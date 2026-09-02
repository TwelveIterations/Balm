package net.blay09.mods.balm.fabric.client.internal.config;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.ModInfo;
import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

public class FabricModListScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.balm.mods.title");
    private static final Component DISCLAIMER = Component.translatable("gui.balm.mods.disclaimer");
    private static final Component SEARCH_LABEL = Component.translatable("gui.balm.configuration.search");
    private static final Component SEARCH_HINT = SEARCH_LABEL.copy().withStyle(EditBox.SEARCH_HINT_STYLE);
    private final Screen parent;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 36, 50);
    private EditBox searchBox;
    private ConfigList list;

    public FabricModListScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        final var header = layout.addToHeader(LinearLayout.vertical().spacing(4));
        header.defaultCellSetting().alignHorizontallyCenter();
        header.addChild(new StringWidget(title, font));
        searchBox = header.addChild(new EditBox(font, 200, 15, SEARCH_LABEL));
        searchBox.setHint(SEARCH_HINT);
        searchBox.setResponder(filter -> {
            if (list != null) {
                list.populateChildren(filter);
                list.setScrollAmount(0);
            }
        });

        list = layout.addToContents(new ConfigList(this));

        final var footer = layout.addToFooter(LinearLayout.vertical().spacing(4));
        footer.defaultCellSetting().alignHorizontallyCenter();
        footer.addChild(new StringWidget(DISCLAIMER, font));
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, _ -> onClose()).build());
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
        if (list != null) {
            list.updateSize(width, layout);
        }
    }

    @Override
    public void onClose() {
        minecraft.gui.setScreen(parent);
    }

    @Override
    protected void setInitialFocus() {
        setInitialFocus(searchBox);
    }

    private static class ConfigList extends ContainerObjectSelectionList<ConfigEntry> {
        private final FabricModListScreen screen;

        ConfigList(FabricModListScreen screen) {
            super(Minecraft.getInstance(), screen.width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 24);
            this.screen = screen;
            populateChildren("");
        }

        void populateChildren(String filter) {
            clearEntries();
            final var normalizedFilter = filter.toLowerCase(java.util.Locale.ROOT);
            BalmConfigScreenProviders.getConfigurableModIds().stream()
                    .map(modId -> new ConfigEntry(screen, modId))
                    .filter(entry -> entry.matches(normalizedFilter))
                    .forEach(this::addEntry);
        }

        @Override
        public int getRowWidth() {
            return Math.min(320, screen.width - 40);
        }
    }

    private static class ConfigEntry extends ContainerObjectSelectionList.Entry<ConfigEntry> {
        private final Button button;
        private final String searchableName;

        ConfigEntry(FabricModListScreen parent, String modId) {
            final var name = Balm.platform().getModInfo(modId).map(ModInfo::name).orElse(modId);
            searchableName = name.toLowerCase(java.util.Locale.ROOT);
            button = Button.builder(Component.literal(name), _ -> {
                final var factory = BalmConfigScreenProviders.getFactory(modId);
                if (factory != null) {
                    Minecraft.getInstance().gui.setScreen(factory.create(parent));
                }
            }).build();
        }

        boolean matches(String filter) {
            return searchableName.contains(filter);
        }

        @Override
        public List<? extends Button> children() {
            return List.of(button);
        }

        @Override
        public List<? extends Button> narratables() {
            return List.of(button);
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
            button.setPosition(getContentX(), getContentY());
            button.setWidth(getContentWidth());
            button.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }
    }
}
