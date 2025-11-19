package net.blay09.mods.balm.forge.capability;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.blay09.mods.balm.forge.ModBusEventRegister;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public record ForgeBalmCapabilities() implements BalmCapabilities {
    private static final Map<Identifier, Capability<?>> backingTypes = new ConcurrentHashMap<>();
    private static final Map<Identifier, CapabilityType<?, ?, ?>> types = new ConcurrentHashMap<>();
    private static final List<BlockEntityProviderRegistration<?, ?>> blockEntityProviders = new CopyOnWriteArrayList<>();
    private static final List<BlockEntityFallbackProviderRegistration<?, ?>> fallbackBlockEntityProviders = new CopyOnWriteArrayList<>();

    private static Multimap<BlockEntityType<?>, BlockEntityProviderRegistration<?, ?>> flattenedBlockEntityProviders;

    public ForgeBalmCapabilities {
        AttachCapabilitiesEvent.BlockEntities.BUS.addListener(this::attachBlockEntityCapabilities);
    }

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

    public <TApi> void preRegisterType(Identifier identifier, CapabilityToken<TApi> capabilityToken) {
        preRegisterType(identifier, CapabilityManager.get(capabilityToken));
    }

    public <TApi> void preRegisterType(Identifier identifier, Capability<TApi> capability) {
        backingTypes.put(identifier, capability);
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(Identifier identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        getRegistrations(identifier.getNamespace()).apiClasses.add(apiClass);
        final var backingType = backingTypes.get(identifier);
        if (backingType == null) {
            throw new IllegalStateException(
                    "You must additionally call ForgeBalmCapabilities.preRegisterType() on Forge first, as Balm cannot create a capability token dynamically.");
        }
        final var type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, backingType);
        types.put(identifier, type);
        return type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> getType(Identifier identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        var type = types.get(identifier);
        if (type == null) {
            final var backingType = backingTypes.get(identifier);
            if (backingType == null) {
                throw new IllegalStateException(
                        "You must call ForgeBalmCapabilities.preRegisterType() on Forge first, as Balm cannot create a capability token dynamically.");
            }
            type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, backingType);
            types.put(identifier, type);
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
    public <TApi, TContext> void registerProvider(Identifier identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, Supplier<Set<BlockEntityType<?>>> blockEntityTypes) {
        blockEntityProviders.add(new BlockEntityProviderRegistration<>(identifier, type, provider, blockEntityTypes));
        flattenedBlockEntityProviders = null;
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(Identifier identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        fallbackBlockEntityProviders.add(new BlockEntityFallbackProviderRegistration<>(identifier, type, provider));
    }

    private void attachBlockEntityCapabilities(AttachCapabilitiesEvent.BlockEntities event) {
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
        int i = 0;
        for (final var blockEntityProvider : flattenedBlockEntityProviders.get(blockEntity.getType())) {
            event.addCapability(blockEntityProvider.identifier().withSuffix("_" + i++),
                    new BlockEntityCapabilityProvider(blockEntity, blockEntityProvider.type(), blockEntityProvider.provider()));
        }

        i = 0;
        for (final var fallbackBlockEntityProvider : fallbackBlockEntityProviders) {
            event.addCapability(fallbackBlockEntityProvider.identifier().withSuffix("_" + i++),
                    new BlockEntityCapabilityProvider(blockEntity, fallbackBlockEntityProvider.type(), fallbackBlockEntityProvider.provider()));
        }
    }

    public <TApi> CapabilityType<Block, TApi, Direction> addExistingType(Identifier identifier, Class<TApi> apiClass, Capability<TApi> capability) {
        final var type = new CapabilityType<>(identifier,
                Block.class,
                apiClass,
                Direction.class,
                capability);
        types.put(identifier, type);
        return type;
    }

    private Registrations getRegistrations(String namespace) {
        return ModBusEventRegisters.getRegistrations(namespace, Registrations.class);
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

    record BlockEntityProviderRegistration<TApi, TContext>(Identifier identifier, CapabilityType<Block, TApi, TContext> type,
                                                           BiFunction<BlockEntity, TContext, TApi> provider,
                                                           Supplier<Set<BlockEntityType<?>>> blockEntityTypes) {
    }

    record BlockEntityFallbackProviderRegistration<TApi, TContext>(Identifier identifier, CapabilityType<Block, TApi, TContext> type,
                                                                   BiFunction<BlockEntity, TContext, TApi> provider) {
    }


    public static class Registrations implements ModBusEventRegister {

        private final List<Class<?>> apiClasses = new ArrayList<>();

        private void registerCapabilities(final RegisterCapabilitiesEvent event) {
            for (final var apiClass : apiClasses) {
                event.register(apiClass);
            }
        }

        @Override
        public void register(BusGroup busGroup) {
            RegisterCapabilitiesEvent.BUS.addListener(this::registerCapabilities);
        }
    }
}
