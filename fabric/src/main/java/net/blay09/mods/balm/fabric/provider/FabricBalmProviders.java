package net.blay09.mods.balm.fabric.provider;

import net.blay09.mods.balm.api.provider.BalmProviders;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricBalmProviders implements BalmProviders {

    private final Map<Class<?>, ResourceLocation> lookupIds = new ConcurrentHashMap<>();

    @Override
    public <T> T getProvider(BlockEntity blockEntity, Class<T> clazz) {
        BlockApiLookup<T, Void> lookup = BlockApiLookup.get(lookupId(clazz), clazz, Void.class);
        return lookup.find(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, null);
    }

    @Override
    public <T> T getProvider(BlockEntity blockEntity, Direction direction, Class<T> clazz) {
        BlockApiLookup<T, Direction> lookup = BlockApiLookup.get(lookupId(clazz), clazz, Direction.class);
        return lookup.find(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction);
    }

    @Override
    public <T> T getProvider(Entity entity, Class<T> clazz) {
        var lookup = EntityApiLookup.get(lookupId(clazz), clazz, Void.class);
        return lookup.find(entity, null);
    }

    public void registerProvider(ResourceLocation lookupId, Class<?> clazz) {
        lookupIds.put(clazz, lookupId);
    }

    private ResourceLocation lookupId(Class<?> clazz) {
        return lookupIds.get(clazz);
    }
}
