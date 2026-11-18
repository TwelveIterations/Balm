package net.blay09.mods.balm.fabric.client.internal;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.minecraft.client.core.registries.BuiltInClientRegistries;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class FabricBalmRangeSelectItemModelPropertyRegistrar implements BalmRangeSelectItemModelPropertyRegistrar {

    public static final FabricBalmRangeSelectItemModelPropertyRegistrar INSTANCE = new FabricBalmRangeSelectItemModelPropertyRegistrar();

    @Override
    public void register(Identifier id, MapCodec<? extends RangeSelectItemModelProperty> mapCodec) {
        Registry.register(BuiltInClientRegistries.RANGE_SELECT_ITEM_MODEL_PROPERTY_TYPE, id, mapCodec);
    }
}
