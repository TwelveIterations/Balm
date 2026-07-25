package net.blay09.mods.balm.client.platform.config.screen;

import net.blay09.mods.balm.client.platform.config.screen.internal.BalmConfigScreenSection;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BalmConfigScreenSearch {
    public static boolean componentMatches(Component component, String query) {
        return query.isEmpty() || component.getString().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
    }

    public static boolean propertyMatches(ConfiguredProperty<?> property, String query) {
        return query.isEmpty()
                || componentMatches(Component.translatable(ConfigLocalization.forProperty(property)), query)
                || componentMatches(Component.translatable(ConfigLocalization.forPropertyTooltip(property)), query)
                || property.name().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
    }

    public static boolean categoryMatches(ConfigCategory category, String query) {
        return query.isEmpty()
                || componentMatches(Component.translatable(ConfigLocalization.forCategory(category)), query)
                || componentMatches(Component.translatable(ConfigLocalization.forCategoryTooltip(category)), query)
                || category.name().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
                || category.properties().stream().anyMatch(property -> propertyMatches(property, query));
    }

    public static List<BalmConfigScreenSection> filterSections(List<BalmConfigScreenSection> sections, String query, BalmConfigScreenContext context) {
        final var filteredSections = new ArrayList<BalmConfigScreenSection>();
        for (final var section : sections) {
            final var sectionMatches = componentMatches(section.title(), query);
            final var matchingRows = section.rows().stream()
                    .filter(row -> row.isVisible(context))
                    .filter(row -> sectionMatches || row.matchesFilter(query))
                    .toList();
            if (!matchingRows.isEmpty()) {
                filteredSections.add(new BalmConfigScreenSection(section.title(), matchingRows));
            }
        }
        return filteredSections;
    }
}
