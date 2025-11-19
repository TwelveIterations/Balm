package net.blay09.mods.balm.event.internal;

import net.blay09.mods.balm.event.BidirectionalEventMapper;
import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.event.EventPhases;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EventMapperImpl<TCallback> implements EventMapper<TCallback>, BidirectionalEventMapper<TCallback> {

    private final String name;

    @Nullable
    private Supplier<TCallback> invoker;

    @Nullable
    private BiConsumer<ResourceLocation, TCallback> registrar;

    public EventMapperImpl(String name) {
        this.name = name;
    }

    @Override
    public void register(TCallback listener) {
        register(EventPhases.DEFAULT, listener);
    }

    @Override
    public void register(ResourceLocation phase, TCallback listener) {
        if (registrar == null) {
            throw new IllegalStateException("Event has not been bound.");
        }

        registrar.accept(phase, listener);
    }

    @Override
    public TCallback invoker() {
        if (invoker == null) {
            throw new IllegalStateException("Event cannot be invoked.");
        }

        return invoker.get();
    }

    @Override
    public void configureMapping(BiConsumer<ResourceLocation, TCallback> registrar) {
        this.registrar = registrar;
    }

    @Override
    public void configureMapping(BiConsumer<ResourceLocation, TCallback> registrar, Supplier<TCallback> invoker) {
        this.registrar = registrar;
        this.invoker = invoker;
    }

    @Override
    public String name() {
        return name;
    }

}
