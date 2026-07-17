package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

import java.util.List;

public class BalmConfigScreenList extends ContainerObjectSelectionList<BalmConfigScreenEntry> {
    public static final int MAX_ROW_WIDTH = 520;
    public static final int MIN_SCREEN_PADDING_X = 20;
    public static final int ROW_HEIGHT = 24;

    private final BalmConfigScreen screen;
    private final BalmConfigScreenControlFactory controlFactory;
    private final List<BalmConfigScreenSection> sections;

    public BalmConfigScreenList(BalmConfigScreen screen, BalmConfigScreenControlFactory controlFactory, List<BalmConfigScreenSection> sections) {
        super(Minecraft.getInstance(), screen.width, screen.contentHeight(), screen.headerHeight(), ROW_HEIGHT);
        this.screen = screen;
        this.controlFactory = controlFactory;
        this.sections = sections;
        populateChildren("");
    }

    @Override
    public int getRowWidth() {
        return Math.min(MAX_ROW_WIDTH, screen.width - MIN_SCREEN_PADDING_X * 2);
    }

    public void populateChildren(String filter) {
        clearEntries();
        for (final var section : BalmConfigScreenSearch.filterSections(sections, filter, screen)) {
            addEntry(new BalmConfigScreenHeadingEntry(screen, section.title().copy().withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)));
            section.rows().forEach(row -> addEntry(createEntry(row)));
        }
    }

    private BalmConfigScreenEntry createEntry(BalmConfigScreenRow row) {
        return switch (row) {
            case BalmConfigScreenPropertyRow propertyRow ->
                    new BalmConfigScreenPropertyEntry(screen, propertyRow.property(), controlFactory.createControl(propertyRow.property(), propertyRow.state()));
            case BalmConfigScreenMergedPropertiesRow mergedPropertiesRow ->
                    new BalmConfigScreenMergedPropertiesEntry(screen, mergedPropertiesRow, controlFactory.createMergedPropertiesControl(mergedPropertiesRow));
            case BalmConfigScreenCustomEntryRow customEntryRow ->
                    customEntryRow.entryFactory().apply(screen, customEntryRow.state());
            default ->
                    throw new IllegalStateException("Unsupported configuration row type: " + row.getClass().getName());
        };
    }

}
