package net.blay09.mods.balm.neoforge.client.internal;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;

public class NeoForgeBalmRangeSelectItemModelPropertyRegistrar implements BalmRangeSelectItemModelPropertyRegistrar {

    private final RegisterRangeSelectItemModelPropertyEvent event;

    public NeoForgeBalmRangeSelectItemModelPropertyRegistrar(RegisterRangeSelectItemModelPropertyEvent event) {
        this.event = event;
    }

    @Override
    public void register(Identifier id, MapCodec<? extends RangeSelectItemModelProperty> mapCodec) {
        event.register(id, mapCodec);
    }
}
