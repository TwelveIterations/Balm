package net.blay09.mods.balm.forge.client.internal;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.blay09.mods.balm.forge.internal.mixin.RangeSelectItemModelPropertiesAccessor;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;

public class ForgeBalmRangeSelectItemModelPropertyRegistrar implements BalmRangeSelectItemModelPropertyRegistrar {

    public static final ForgeBalmRangeSelectItemModelPropertyRegistrar INSTANCE = new ForgeBalmRangeSelectItemModelPropertyRegistrar();

    @Override
    public void register(Identifier id, MapCodec<? extends RangeSelectItemModelProperty> mapCodec) {
        RangeSelectItemModelPropertiesAccessor.getIdMapper().put(id, mapCodec);
    }
}
