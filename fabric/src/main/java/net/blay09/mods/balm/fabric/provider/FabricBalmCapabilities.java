package net.blay09.mods.balm.fabric.provider;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class FabricBalmCapabilities implements BalmCapabilities {

    @Override
    public <TApi, TContext> CapabilityType<TApi, TContext> getType(ResourceLocation identifier, Class<TApi> apiClass, Class<TContext> contextClass) {
        final var lookup = BlockApiLookup.get(identifier, apiClass, contextClass);
        return new CapabilityType<>(identifier, apiClass, contextClass, lookup);
    }

    @Override
    public <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context, CapabilityType<TApi, TContext> type) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        return lookup.find(level, pos, state, blockEntity, context);
    }

    @Override
    public <TApi, TContext> void registerProvider(CapabilityType<TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, BlockEntityType<?>... blockEntityTypes) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        lookup.registerForBlockEntities(provider::apply, blockEntityTypes);
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(CapabilityType<TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        lookup.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            @Override
            public @Nullable TApi find(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context) {
                return blockEntity != null ? provider.apply(blockEntity, context) : null;
            }
        });
    }

}
