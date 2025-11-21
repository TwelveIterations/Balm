package net.blay09.mods.balm.platform.event.internal;

import net.blay09.mods.balm.platform.event.AsymmetricalEventMapper;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.blay09.mods.balm.platform.event.EventPhases;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AsymmetricalEventMapperImpl<TCallback, TInvoker> implements EventMapper<TCallback>, AsymmetricalEventMapper<TCallback, TInvoker> {

    private final String name;

    @Nullable
    private Supplier<TInvoker> invoker;

    @Nullable
    private BiConsumer<Identifier, TCallback> registrar;

    public AsymmetricalEventMapperImpl(String name) {
        this.name = name;
    }

    @Override
    public void register(TCallback listener) {
        register(EventPhases.DEFAULT, listener);
    }

    @Override
    public void register(Identifier phase, TCallback listener) {
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
    public void configureMapping(BiConsumer<Identifier, TCallback> registrar) {
        this.registrar = registrar;
    }

    @Override
    public void configureMapping(BiConsumer<Identifier, TCallback> registrar, Supplier<TInvoker> invoker) {
        this.registrar = registrar;
        this.invoker = invoker;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public EventMapper<TCallback> filter(String name, Function<TCallback, TCallback> filter) {
        final EventMapper<TCallback> mapper = EventMapper.createUnbound(name() + "(" + name + ")");
        mapper.configureMapping((phase, it) -> this.register(phase, filter.apply(it)));
        return mapper;
    }
}
