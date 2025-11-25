package net.blay09.mods.balm.api.provider;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities} instead.
 */
@Deprecated(since = "1.21.5")
public class ProviderUtils {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities} instead.
     */
    @Nullable
    @Deprecated(since = "1.21.5")
    public static <T> T getProvider(BlockEntity blockEntity, Class<T> clazz) {
        return Balm.getProviders().getProvider(blockEntity, clazz);
    }
}
