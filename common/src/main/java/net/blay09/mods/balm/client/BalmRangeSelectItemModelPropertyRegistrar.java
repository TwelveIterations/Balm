package net.blay09.mods.balm.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;

public interface BalmRangeSelectItemModelPropertyRegistrar {

    void register(Identifier id, MapCodec<? extends RangeSelectItemModelProperty> mapCodec);
}
