package net.blay09.mods.balm.event.internal;

import net.blay09.mods.balm.event.AsymmetricalEventMapper;
import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.event.EventPhases;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class AsymmetricalEventMapperImpl<TCallback, TInvoker> implements EventMapper<TCallback>, AsymmetricalEventMapper<TCallback, TInvoker> {

    private final String name;

    @Nullable
    private Supplier<TInvoker> invoker;

    @Nullable
    private BiConsumer<ResourceLocation, TCallback> registrar;

    public AsymmetricalEventMapperImpl(String name) {
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
    public TInvoker invoker() {
        if (invoker == null) {
            throw new IllegalStateException("Event cannot be invoked.");
        }

        return invoker.get();
    }

    @Override
    public void setup(BiConsumer<ResourceLocation, TCallback> registrar) {
        this.registrar = registrar;
    }

    @Override
    public void setup(BiConsumer<ResourceLocation, TCallback> registrar, Supplier<TInvoker> invoker) {
        this.registrar = registrar;
        this.invoker = invoker;
    }

    @Override
    public boolean hasBeenSetup() {
        return registrar != null;
    }

    @Override
    public String name() {
        return name;
    }
}
