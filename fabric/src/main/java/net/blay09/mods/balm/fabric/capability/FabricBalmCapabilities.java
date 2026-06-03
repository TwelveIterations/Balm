package net.blay09.mods.balm.fabric.capability;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FabricBalmCapabilities implements BalmCapabilities {

    private final Map<ResourceLocation, CapabilityType<?, ?, ?>> types = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> getType(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
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
    @Nullable
    public <TApi, TContext> TApi getCapability(Entity entity, @Nullable TContext context, CapabilityType<Entity, TApi, TContext> type) {
        @SuppressWarnings("unchecked") final var lookup = (EntityApiLookup<TApi, TContext>) type.backingType();
        return lookup.find(entity, context);
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        final Object lookup;
        if (scopeClass == Block.class) {
            lookup = BlockApiLookup.get(identifier, apiClass, contextClass);
        } else if (scopeClass == Entity.class) {
            lookup = EntityApiLookup.get(identifier, apiClass, contextClass);
        } else {
            throw new IllegalArgumentException("Unsupported scope class: " + scopeClass);
        }
        final var type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, lookup);
        types.put(identifier, type);
        return type;
    }

    @Override
    public <TApi, TContext> void registerProvider(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, Supplier<List<BlockEntityType<?>>> blockEntityTypes) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        lookup.registerForBlockEntities(provider::apply, blockEntityTypes.get().toArray(BlockEntityType[]::new));
    }

    @Override
    public <TApi, TContext> void registerEntityProvider(ResourceLocation identifier, CapabilityType<Entity, TApi, TContext> type, BiFunction<Entity, TContext, TApi> provider, Supplier<List<EntityType<?>>> entityTypes) {
        @SuppressWarnings("unchecked") final var lookup = (EntityApiLookup<TApi, TContext>) type.backingType();
        lookup.registerForTypes(provider::apply, entityTypes.get().toArray(EntityType[]::new));
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        @SuppressWarnings("unchecked") final var lookup = (BlockApiLookup<TApi, TContext>) type.backingType();
        lookup.registerFallback(new BlockApiLookup.BlockApiProvider<>() {
            @Override
            public @Nullable TApi find(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context) {
                return blockEntity != null ? provider.apply(blockEntity, context) : null;
            }
        });
    }

    @Override
    public <TApi, TContext> void registerFallbackEntityProvider(ResourceLocation identifier, CapabilityType<Entity, TApi, TContext> type, BiFunction<Entity, TContext, TApi> provider) {
        @SuppressWarnings("unchecked") final var lookup = (EntityApiLookup<TApi, TContext>) type.backingType();
        lookup.registerFallback(provider::apply);
    }

}
