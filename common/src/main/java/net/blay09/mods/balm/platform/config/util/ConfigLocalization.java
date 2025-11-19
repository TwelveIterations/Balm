package net.blay09.mods.balm.platform.config.util;

import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigCategory;

public class ConfigLocalization {
    public static String forTitle(BalmConfigSchema schema) {
        return schema.identifier().getNamespace() + ".configuration." + schema.identifier().getPath() + ".title";
    }

    public static String forTitle(String modId) {
        return modId + ".configuration.title";
    }

    public static String forRootCategory(BalmConfigSchema schema) {
        return schema.identifier().getNamespace() + ".configuration";
    }

    public static String forCategory(ConfigCategory category) {
        return category.parentSchema().identifier().getNamespace() + ".configuration." + category.name();
    }

    public static String forProperty(ConfiguredProperty<?> property) {
        if (property.category().isEmpty()) {
            return property.parentSchema().identifier().getNamespace() + ".configuration." + property.name();
        } else {
            return property.parentSchema().identifier().getNamespace() + ".configuration." + property.category() + "." + property.name();
        }
    }

    public static String forPropertyTooltip(ConfiguredProperty<?> property) {
        return forProperty(property) + ".tooltip";
    }
}
