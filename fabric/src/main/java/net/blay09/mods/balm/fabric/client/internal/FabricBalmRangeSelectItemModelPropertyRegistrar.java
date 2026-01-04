package net.blay09.mods.balm.fabric.client.internal;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;

public class FabricBalmRangeSelectItemModelPropertyRegistrar implements BalmRangeSelectItemModelPropertyRegistrar {

    public static final FabricBalmRangeSelectItemModelPropertyRegistrar INSTANCE = new FabricBalmRangeSelectItemModelPropertyRegistrar();

    @Override
    public void register(Identifier id, MapCodec<? extends RangeSelectItemModelProperty> mapCodec) {
        RangeSelectItemModelProperties.ID_MAPPER.put(id, mapCodec);
    }
}
