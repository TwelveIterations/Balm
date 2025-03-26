package net.blay09.mods.balm.common.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.ResourceConditionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

public record ConfigResourceCondition(ResourceLocation configId, String category, String key, String value) implements BalmResourceCondition {

    public static final MapCodec<ConfigResourceCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("config").forGetter(ConfigResourceCondition::configId),
            Codec.STRING.fieldOf("category").orElse("").forGetter(ConfigResourceCondition::category),
            Codec.STRING.fieldOf("key").forGetter(ConfigResourceCondition::key),
            Codec.STRING.fieldOf("value").forGetter(ConfigResourceCondition::value)
    ).apply(instance, ConfigResourceCondition::new));

    @Override
    public boolean test(ResourceConditionContext context) {
        final var schema = Balm.getConfig().getSchema(configId);
        final var config = Balm.getConfig().getActiveConfig(schema);
        final var property = schema.findProperty(category, key);
        if (property != null) {
            final var rawValue = config.getRaw(property);
            String stringValue;
            if (rawValue instanceof StringRepresentable stringRepresentable) {
                stringValue = stringRepresentable.getSerializedName();
            } else {
                stringValue = rawValue.toString();
            }
            return stringValue.equals(value);
        }
        return false;
    }
}
