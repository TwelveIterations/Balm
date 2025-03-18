package net.blay09.mods.balm.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public interface BalmCapabilities {
    <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context, CapabilityType<TApi, TContext> type);

    <TApi, TContext> CapabilityType<TApi, TContext> getType(ResourceLocation identifier, Class<TApi> apiClass, Class<TContext> contextClass);

    default <TApi> TApi getCapability(BlockEntity blockEntity, CapabilityType<TApi, ?> type) {
        return getCapability(blockEntity, null, type);
    }

    default <TApi, TContext> TApi getCapability(BlockEntity blockEntity, TContext context, CapabilityType<TApi, TContext> type) {
        return getCapability(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, context, type);
    }

    <TApi, TContext> void registerProvider(CapabilityType<TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, BlockEntityType<?>... blockEntityTypes);

    /**
     * On Fabric and Forge, this registers as a fallback provider.
     * NeoForge does not support fallback providers, so there, this method will register the provider at lowest priority for each block entity individually (sigh).
     */
    <TApi, TContext> void registerFallbackBlockEntityProvider(CapabilityType<TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider);
}
