package net.blay09.mods.balm.neoforge.provider;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class NeoForgeBalmCapabilities implements BalmCapabilities {
    @Override
    public <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context, CapabilityType<TApi, TContext> type) {
        @SuppressWarnings("unchecked") final var capability = (BlockCapability<TApi, TContext>) type.backingType();
        return level.getCapability(capability, pos, state, blockEntity, context);
    }

    @Override
    public <TApi, TContext> CapabilityType<TApi, TContext> getType(ResourceLocation identifier, Class<TApi> apiClass, Class<TContext> contextClass) {
        return null; // TODO 1.21.5 Capabilities
    }

    @Override
    public <TApi, TContext> void registerProvider(CapabilityType<TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, BlockEntityType<?>... blockEntityTypes) {
        // TODO 1.21.5 Capabilities
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(CapabilityType<TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        // TODO 1.21.5 Capabilities
    }

    public <TApi, TContext> void addExistingType(ResourceLocation identifier, BaseCapability<TApi, TContext> capability) {
        // TODO 1.21.5 Capabilities: We need a scope parameter in types or something because e.g. ItemHandler can exist on both items and block entities
    }
}
