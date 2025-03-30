package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;

import java.util.HashSet;
import java.util.Set;

public class ConfigLocalization {

    private static final Set<String> modernTranslationKeyMods = new HashSet<>();

    public static void enableModernTranslationKeys(String modId) {
        modernTranslationKeyMods.add(modId);
    }

    private static boolean usesLegacyTranslationKeys(String modId) {
        return !modernTranslationKeyMods.contains(modId);
    }

    private static boolean usesLegacyTranslationKeys(BalmConfigSchema schema) {
        return !modernTranslationKeyMods.contains(schema.identifier().getNamespace());
    }

    public static String forTitle(BalmConfigSchema schema) {
        final var modId = schema.identifier().getNamespace();
        if (usesLegacyTranslationKeys(schema)) {
            return "config." + modId + "." + schema.identifier().getPath() + ".title";
        }

        return modId + ".configuration." + schema.identifier().getPath() + ".title";
    }

    public static String forTitle(String modId) {
        if (usesLegacyTranslationKeys(modId)) {
            return "config." + modId + ".title";
        }

        return modId + ".configuration.title";
    }

    public static String forRootCategory(BalmConfigSchema schema) {
        final var modId = schema.identifier().getNamespace();
        if (usesLegacyTranslationKeys(modId)) {
            return "config." + modId;
        }

        return modId + ".configuration";
    }

    public static String forCategory(ConfigCategory category) {
        final var modId = category.parentSchema().identifier().getNamespace();
        if (usesLegacyTranslationKeys(modId)) {
            return "config." + modId + "." + category.name();
        }

        return modId + ".configuration." + category.name();
    }

    public static String forProperty(ConfiguredProperty<?> property) {
        final var modId = property.parentSchema().identifier().getNamespace();
        if (usesLegacyTranslationKeys(modId)) {
            if (property.category().isEmpty()) {
                return "config." + modId + "." + property.name();
            } else {
                return "config." + modId + "." + property.category() + "." + property.name();
            }
        }

        if (property.category().isEmpty()) {
            return modId + ".configuration." + property.name();
        } else {
            return modId + ".configuration." + property.category() + "." + property.name();
        }
    }

    public static String forPropertyTooltip(ConfiguredProperty<?> property) {
        return forProperty(property) + ".tooltip";
    }
}
