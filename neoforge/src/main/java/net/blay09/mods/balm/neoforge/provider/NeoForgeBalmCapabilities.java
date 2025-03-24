package net.blay09.mods.balm.neoforge.provider;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NeoForgeBalmCapabilities implements BalmCapabilities {

    private final Map<ResourceLocation, CapabilityType<?, ?, ?>> types = new HashMap<>();
    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public <TApi, TContext> TApi getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, TContext context, CapabilityType<Block, TApi, TContext> type) {
        @SuppressWarnings("unchecked") final var capability = (BlockCapability<TApi, TContext>) type.backingType();
        return level.getCapability(capability, pos, state, blockEntity, context);
    }

    @Override
    public <TScope, TApi, TContext> CapabilityType<TScope, TApi, TContext> registerType(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass) {
        if (scopeClass == Block.class) {
            final var capability = BlockCapability.create(identifier, apiClass, contextClass);
            final var type = new CapabilityType<>(identifier, scopeClass, apiClass, contextClass, capability);
            types.put(identifier, type);
            return type;
        } else {
            throw new IllegalArgumentException("Unsupported scope class: " + scopeClass);
        }
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
        getRegistrations(identifier.getNamespace()).blockEntityProviders.add(new BlockEntityProviderRegistration<>(type, provider, blockEntityTypes));
    }

    @Override
    public <TApi, TContext> void registerFallbackBlockEntityProvider(ResourceLocation identifier, CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider) {
        getRegistrations(identifier.getNamespace()).fallbackBlockEntityProviders.add(new BlockEntityFallbackProviderRegistration<>(type, provider));
    }

    public <TApi, TContext> void addExistingType(ResourceLocation identifier, BaseCapability<TApi, TContext> capability) {
        if (capability instanceof BlockCapability) {
            types.put(identifier, new CapabilityType<>(identifier,
                    Block.class,
                    capability.typeClass(),
                    capability.contextClass(),
                    capability));
        } else {
            throw new IllegalArgumentException("Unsupported capability type " + capability.getClass());
        }
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

    record BlockEntityProviderRegistration<TApi, TContext>(CapabilityType<Block, TApi, TContext> type, BiFunction<BlockEntity, TContext, TApi> provider,
                                                           Supplier<List<BlockEntityType<?>>> blockEntityTypes) {
    }

    record BlockEntityFallbackProviderRegistration<TApi, TContext>(CapabilityType<Block, TApi, TContext> type,
                                                                   BiFunction<BlockEntity, TContext, TApi> provider) {
    }

    private static class Registrations {

        public final List<BlockEntityProviderRegistration<?, ?>> blockEntityProviders = new ArrayList<>();
        public final List<BlockEntityFallbackProviderRegistration<?, ?>> fallbackBlockEntityProviders = new ArrayList<>();

        @SubscribeEvent
        public void registerCapabilities(final RegisterCapabilitiesEvent event) {
            for (final var blockEntityProvider : blockEntityProviders) {
                doRegister(event, blockEntityProvider);
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void registerFallbackCapabilities(final RegisterCapabilitiesEvent event) {
            for (final var fallbackBlockEntityProvider : fallbackBlockEntityProviders) {
                doRegister(event, fallbackBlockEntityProvider);
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
    }

}
