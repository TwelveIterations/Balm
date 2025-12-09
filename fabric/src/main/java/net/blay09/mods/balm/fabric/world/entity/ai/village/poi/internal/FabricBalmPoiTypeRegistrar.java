package net.blay09.mods.balm.fabric.world.entity.ai.village.poi.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.fabric.internal.mixin.PoiTypesAccessor;
import net.blay09.mods.balm.world.entity.ai.village.poi.internal.BalmPoiTypeRegistrarImpl;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.function.Supplier;

public class FabricBalmPoiTypeRegistrar extends BalmPoiTypeRegistrarImpl {

    public FabricBalmPoiTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T extends PoiType> Holder<T> register(String name, Supplier<T> supplier) {
        final Holder<T> holder = super.register(name, supplier);
        PoiTypesAccessor.callRegisterBlockStates((Holder<PoiType>) holder, holder.value().matchingStates());
        return holder;
    }
}
