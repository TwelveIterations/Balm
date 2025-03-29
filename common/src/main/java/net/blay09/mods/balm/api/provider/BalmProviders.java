package net.blay09.mods.balm.api.provider;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities} instead.
 */
@Deprecated
public interface BalmProviders {
    <T> T getProvider(BlockEntity blockEntity, Class<T> clazz);

    <T> T getProvider(BlockEntity blockEntity, Direction direction, Class<T> clazz);

    <T> T getProvider(Entity entity, Class<T> clazz);
}
