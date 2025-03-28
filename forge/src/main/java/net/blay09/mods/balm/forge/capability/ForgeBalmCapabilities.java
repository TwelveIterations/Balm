package net.blay09.mods.balm.forge.capability;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ForgeBalmCapabilities implements BalmCapabilities {
    private final Map<ResourceLocation, Capability<?>> backingTypes = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, CapabilityType<?, ?, ?>> types = new ConcurrentHashMap<>();
    private final List<BlockEntityProviderRegistration<?, ?>> blockEntityProviders = new CopyOnWriteArrayList<>();
    private final List<BlockEntityFallbackProviderRegistration<?, ?>> fallbackBlockEntityProviders = new CopyOnWriteArrayList<>();
    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    private Multimap<BlockEntityType<?>, BlockEntityProviderRegistration<?, ?>> flattenedBlockEntityProviders;

    @Override
    public <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context, CapabilityType<Block, TApi, TContext> type) {
        if (blockEntity != null) {
            @SuppressWarnings("unchecked") final var capability = (Capability<TApi>) type.backingType();
            if (context == null) {
                return blockEntity.getCapability(capability).resolve().orElse(null);
            } else if (context instanceof Direction direction) {
                return blockEntity.getCapability(capability, direction).resolve().orElse(null);
            }
        }
        return null;
    }

    public <TApi> void preRegisterType(ResourceLocation identifier, Capability<TApi> capability) {
        backingTypes.put(identifier, capability);
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        getRegistrations(identifier.getNamespace()).apiClasses.add(apiClass);
        final var backingType =  backingTypes.get(identifier);
        if(backingType == null) {
            throw new IllegalStateException("You must additionally call ForgeBalmCapabilities.preRegisterType() on Forge first, as Balm cannot create a capability token dynamically.");
        }
        final var type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, backingType);
        types.put(identifier, type);
        return type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> getType(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        final var type = types.get(identifier);
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
    public <TApi, TContext> void registerProvider(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, Supplier<List<BlockEntityType<?>>> blockEntityTypes) {
        blockEntityProviders.add(new BlockEntityProviderRegistration<>(identifier, type, provider, new Supplier<>() {
            private Set<BlockEntityType<?>> set;

            @Override
            public Set<BlockEntityType<?>> get() {
                if (set == null) {
                    set = Set.copyOf(blockEntityTypes.get());
                }
                return set;
            }
        }));
        flattenedBlockEntityProviders = null;
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        fallbackBlockEntityProviders.add(new BlockEntityFallbackProviderRegistration<>(identifier, type, provider));
    }

    @SubscribeEvent
    public void attachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (flattenedBlockEntityProviders == null) {
            flattenedBlockEntityProviders = ArrayListMultimap.create();
            for (final var blockEntityProvider : blockEntityProviders) {
                final var blockEntityTypes = blockEntityProvider.blockEntityTypes.get();
                for (final var blockEntityType : blockEntityTypes) {
                    flattenedBlockEntityProviders.put(blockEntityType, blockEntityProvider);
                }
            }
        }

        final var blockEntity = event.getObject();
        for (final var blockEntityProvider : flattenedBlockEntityProviders.get(blockEntity.getType())) {
            event.addCapability(blockEntityProvider.identifier(),
                    new BlockEntityCapabilityProvider(blockEntity, blockEntityProvider.type(), blockEntityProvider.provider()));
        }

        for (final var fallbackBlockEntityProvider : fallbackBlockEntityProviders) {
            event.addCapability(fallbackBlockEntityProvider.identifier(),
                    new BlockEntityCapabilityProvider(blockEntity, fallbackBlockEntityProvider.type(), fallbackBlockEntityProvider.provider()));
        }
    }

    public <TApi> void addExistingType(ResourceLocation identifier, Class<TApi> apiClass, Capability<TApi> capability) {
        types.put(identifier, new CapabilityType<>(identifier,
                Block.class,
                apiClass,
                Direction.class,
                capability));
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

    record BlockEntityCapabilityProvider(BlockEntity blockEntity, CapabilityType<Block, ?, ?> type,
                                         BiFunction<BlockEntity, ?, ?> provider) implements ICapabilityProvider {
        @Override
        @SuppressWarnings("unchecked")
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
            final var capability = (Capability<?>) type.backingType();
            if (cap == capability) {
                final T result;
                if (side != null && type.contextClass() == Direction.class) {
                    result = ((BiFunction<BlockEntity, Direction, T>) provider).apply(blockEntity, side);
                } else {
                    result = ((BiFunction<BlockEntity, ?, T>) provider).apply(blockEntity, null);
                }
                if (result != null) {
                    return LazyOptional.of(() -> result);
                }
            }
            return LazyOptional.empty();
        }
    }

    record BlockEntityProviderRegistration<TApi, TContext>(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type,
                                                           BiFunction<BlockEntity, TContext, TApi> provider,
                                                           Supplier<Set<BlockEntityType<?>>> blockEntityTypes) {
    }

    record BlockEntityFallbackProviderRegistration<TApi, TContext>(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type,
                                                                   BiFunction<BlockEntity, TContext, TApi> provider) {
    }


    private static class Registrations {

        private final List<Class<?>> apiClasses = new ArrayList<>();

        @SubscribeEvent
        public void registerCapabilities(final RegisterCapabilitiesEvent event) {
            for (final var apiClass : apiClasses) {
                event.register(apiClass);
            }
        }

    }
}
