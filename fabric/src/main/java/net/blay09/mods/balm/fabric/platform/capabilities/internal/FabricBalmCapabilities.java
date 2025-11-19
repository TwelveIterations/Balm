package net.blay09.mods.balm.fabric.platform.capabilities.internal;

import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.capabilities.CapabilityType;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FabricBalmCapabilities implements BalmCapabilities {

    private final Map<Identifier, CapabilityType<?, ?, ?>> types = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> getType(Identifier identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        var type = types.get(identifier);
        if (type == null) {
            type = registerType(identifier, scopeClass, apiClass, contextClass);
        }
        if (type.scopeClass() != scopeClass) {
            throw new IllegalArgumentException("Incompatible scope for capability " + identifier + ", expected " + type.scopeClass() + " but got " + scopeClass);
        }
        if (type.apiClass() != apiClass) {
            throw new IllegalArgumentException("Incompatible API for capability " + identifier + ", expected " + type.apiClass() + " but got " + apiClass);
        }
        if (type.contextClass() != contextClass) {
            throw new IllegalArgumentException("Incompatible context for capability " + identifier + ", expected " + type.contextClass() + " but got " + contextClass);
        }

        return (CapabilityType<TScope, TApi, TContext>) type;
    }

    @Override
    public <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context, CapabilityType<Block, TApi, TContext> type) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        return lookup.find(level, pos, state, blockEntity, context);
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(Identifier identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        final var lookup = BlockApiLookup.get(identifier, apiClass, contextClass);
        final var type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, lookup);
        types.put(identifier, type);
        return type;
    }

    @Override
    public <TApi, TContext> void registerProvider(Identifier identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, Supplier<Set<BlockEntityType<?>>> blockEntityTypes) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        lookup.registerForBlockEntities(provider::apply, blockEntityTypes.get().toArray(BlockEntityType[]::new));
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(Identifier identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        lookup.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            @Override
            public @Nullable TApi find(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context) {
                return blockEntity != null ? provider.apply(blockEntity, context) : null;
            }
        });
    }

}
