package net.blay09.mods.balm.neoforge.platform.capabilities.internal;

import net.blay09.mods.balm.neoforge.platform.event.internal.ModBusEventRegisters;
import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.capabilities.CapabilityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public record NeoForgeBalmCapabilities() implements BalmCapabilities {

    private static final Map<Identifier, CapabilityType<?, ?, ?>> types = new ConcurrentHashMap<>();

    @Override
    @Nullable
    public <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable TContext context, CapabilityType<Block, TApi, TContext> type) {
        @SuppressWarnings("unchecked") final var capability = (BlockCapability<TApi, TContext>) type.backingType();
        return level.getCapability(capability, pos, state, blockEntity, context);
    }

    @Override
    @Nullable
    public <TApi, TContext> TApi getCapability(Entity entity, @Nullable TContext context, CapabilityType<Entity, TApi, TContext> type) {
        @SuppressWarnings("unchecked") final var capability = (EntityCapability<TApi, TContext>) type.backingType();
        return entity.getCapability(capability, context);
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(Identifier identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        if (scopeClass == Block.class) {
            final var capability = BlockCapability.create(identifier, apiClass, contextClass);
            final var type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, capability);
            types.put(identifier, type);
            return type;
        } else if (scopeClass == Entity.class) {
            final var capability = EntityCapability.create(identifier, apiClass, contextClass);
            final var type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, capability);
            types.put(identifier, type);
            return type;
        } else {
            throw new IllegalArgumentException("Unsupported scope class: " + scopeClass);
        }
    }

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
    public <TApi, TContext> void registerProvider(Identifier identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider, Supplier<Set<BlockEntityType<?>>> blockEntityTypes) {
        getRegistrations(identifier.getNamespace()).blockEntityProviders.add(new BlockEntityProviderRegistration<>(type, provider, blockEntityTypes));
    }

    @Override
    public <TApi, TContext> void registerEntityProvider(Identifier identifier, CapabilityType<Entity, TApi, TContext> type, BiFunction<Entity, TContext, TApi> provider, Supplier<Set<EntityType<?>>> entityTypes) {
        getRegistrations(identifier.getNamespace()).entityProviders.add(new EntityProviderRegistration<>(type, provider, entityTypes));
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(Identifier identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        getRegistrations(identifier.getNamespace()).fallbackBlockEntityProviders.add(new BlockEntityFallbackProviderRegistration<>(type, provider));
    }

    @Override
    public <TApi, TContext> void registerFallbackEntityProvider(Identifier identifier, CapabilityType<Entity, TApi, TContext> type, BiFunction<Entity, TContext, TApi> provider) {
        getRegistrations(identifier.getNamespace()).fallbackEntityProviders.add(new EntityFallbackProviderRegistration<>(type, provider));
    }

    public <TApi, TContext> CapabilityType<Block, TApi, TContext> addExistingType(Identifier identifier, BaseCapability<TApi, TContext> capability) {
        if (capability instanceof BlockCapability) {
            final var type = new CapabilityType<>(identifier,
                    Block.class,
                    capability.typeClass(),
                    capability.contextClass(),
                    capability);
            types.put(identifier, type);
            return type;
        } else {
            throw new IllegalArgumentException("Unsupported capability type " + capability.getClass());
        }
    }

    public <TApi, TContext> CapabilityType<Entity, TApi, TContext> addExistingEntityType(Identifier identifier, BaseCapability<TApi, TContext> capability) {
        if (capability instanceof EntityCapability) {
            final var type = new CapabilityType<>(identifier,
                    Entity.class,
                    capability.typeClass(),
                    capability.contextClass(),
                    capability);
            types.put(identifier, type);
            return type;
        } else {
            throw new IllegalArgumentException("Unsupported capability type " + capability.getClass());
        }
    }

    private Registrations getRegistrations(String namespace) {
        return ModBusEventRegisters.getRegistrations(namespace, Registrations.class);
    }

    public record BlockEntityProviderRegistration<TApi, TContext>(CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, @Nullable TContext, @Nullable TApi> provider,
                                                           Supplier<Set<BlockEntityType<?>>> blockEntityTypes) {
    }

    public record BlockEntityFallbackProviderRegistration<TApi, TContext>(CapabilityType<Block, TApi, TContext> type,
                                                                   BiFunction<BlockEntity, @Nullable TContext, @Nullable TApi> provider) {
    }

    public record EntityProviderRegistration<TApi, TContext>(CapabilityType<Entity, TApi, TContext> type,
                                                             BiFunction<Entity, @Nullable TContext, @Nullable TApi> provider,
                                                             Supplier<Set<EntityType<?>>> entityTypes) {
    }

    public record EntityFallbackProviderRegistration<TApi, TContext>(CapabilityType<Entity, TApi, TContext> type,
                                                                     BiFunction<Entity, @Nullable TContext, @Nullable TApi> provider) {
    }

    public static class Registrations {

        public final List<BlockEntityProviderRegistration<?, ?>> blockEntityProviders = new ArrayList<>();
        public final List<BlockEntityFallbackProviderRegistration<?, ?>> fallbackBlockEntityProviders = new ArrayList<>();
        public final List<EntityProviderRegistration<?, ?>> entityProviders = new ArrayList<>();
        public final List<EntityFallbackProviderRegistration<?, ?>> fallbackEntityProviders = new ArrayList<>();

        @SubscribeEvent
        public void registerCapabilities(final RegisterCapabilitiesEvent event) {
            for (final var blockEntityProvider : blockEntityProviders) {
                doRegister(event, blockEntityProvider);
            }
            for (final var entityProvider : entityProviders) {
                doRegister(event, entityProvider);
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void registerFallbackCapabilities(final RegisterCapabilitiesEvent event) {
            for (final var fallbackBlockEntityProvider : fallbackBlockEntityProviders) {
                doRegister(event, fallbackBlockEntityProvider);
            }
            for (final var fallbackEntityProvider : fallbackEntityProviders) {
                doRegister(event, fallbackEntityProvider);
            }
        }

        private <TApi, TContext> void doRegister(RegisterCapabilitiesEvent event, BlockEntityProviderRegistration<TApi, TContext> registration) {
            final var blocks = registration.blockEntityTypes.get().stream().flatMap(it -> it.getValidBlocks().stream()).distinct().toArray(Block[]::new);
            @SuppressWarnings("unchecked") final var capability = (BlockCapability<TApi, TContext>) registration.type().backingType();
            event.registerBlock(capability, new IBlockCapabilityProvider<>() {
                @Override
                public @Nullable TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable TContext context) {
                    return blockEntity != null ? registration.provider.apply(blockEntity, context) : null;
                }
            }, blocks);
        }

        private <TApi, TContext> void doRegister(RegisterCapabilitiesEvent event, BlockEntityFallbackProviderRegistration<TApi, TContext> registration) {
            @SuppressWarnings("unchecked") final var capability = (BlockCapability<TApi, TContext>) registration.type().backingType();
            final var blocks = BuiltInRegistries.BLOCK_ENTITY_TYPE.stream().flatMap(it -> it.getValidBlocks().stream()).distinct().toArray(Block[]::new);
            event.registerBlock(capability, new IBlockCapabilityProvider<>() {
                @Override
                public @Nullable TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable TContext context) {
                    return blockEntity != null ? registration.provider.apply(blockEntity, context) : null;
                }
            }, blocks);
        }

        private <TApi, TContext> void doRegister(RegisterCapabilitiesEvent event, EntityProviderRegistration<TApi, TContext> registration) {
            @SuppressWarnings("unchecked") final var capability = (EntityCapability<TApi, TContext>) registration.type().backingType();
            for (final var entityType : registration.entityTypes.get()) {
                registerEntity(event, capability, entityType, registration.provider);
            }
        }

        private <TApi, TContext> void doRegister(RegisterCapabilitiesEvent event, EntityFallbackProviderRegistration<TApi, TContext> registration) {
            @SuppressWarnings("unchecked") final var capability = (EntityCapability<TApi, TContext>) registration.type().backingType();
            for (final var entityType : BuiltInRegistries.ENTITY_TYPE) {
                registerEntity(event, capability, entityType, registration.provider);
            }
        }

        @SuppressWarnings("unchecked")
        private <TApi, TContext, TEntity extends Entity> void registerEntity(RegisterCapabilitiesEvent event, EntityCapability<TApi, TContext> capability, EntityType<?> entityType, BiFunction<Entity, @Nullable TContext, @Nullable TApi> provider) {
            event.registerEntity(capability, (EntityType<TEntity>) entityType, (entity, context) -> provider.apply(entity, context));
        }
    }

}
