package net.blay09.mods.balm.neoforge.world.item.internal;

import net.blay09.mods.balm.world.item.BalmCompostableRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeoForgeBalmCompostableRegistrar implements BalmCompostableRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(NeoForgeBalmCompostableRegistrar.class);

    private final Registry<Item> registry;

    public NeoForgeBalmCompostableRegistrar(Registry<Item> registry) {
        this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void register(ItemLike item, float value) {
        final var dataMap = registry.getDataMap(NeoForgeDataMaps.COMPOSTABLES);
        final var resourceKey = item instanceof Holder<?> holder ? (ResourceKey<Item>) holder.unwrapKey().orElseThrow() : item.asItem().builtInRegistryHolder().key();
        try {
            dataMap.put(resourceKey, new Compostable(value));
        } catch (UnsupportedOperationException e) {
            logger.warn("Failed to register compostable item {} with chance {} because the compostables data map is not mutable", resourceKey.identifier(), value);
        }
    }

}
