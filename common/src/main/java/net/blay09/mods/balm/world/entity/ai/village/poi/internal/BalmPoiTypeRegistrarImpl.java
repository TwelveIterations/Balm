package net.blay09.mods.balm.world.entity.ai.village.poi.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.entity.ai.village.poi.BalmPoiTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.function.Supplier;

public class BalmPoiTypeRegistrarImpl implements BalmPoiTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmPoiTypeRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PoiType> Holder<T> register(String name, Supplier<T> supplier) {
        final var identifier = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, identifier);
        return (Holder<T>) registrar.register(resourceKey, id -> supplier.get());
    }
}
