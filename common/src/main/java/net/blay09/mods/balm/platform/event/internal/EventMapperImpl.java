package net.blay09.mods.balm.platform.event.internal;

import net.blay09.mods.balm.platform.event.BidirectionalEventMapper;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.blay09.mods.balm.platform.event.EventPhases;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EventMapperImpl<TCallback> implements EventMapper<TCallback>, BidirectionalEventMapper<TCallback> {

    private final String name;

    @Nullable
    private Supplier<TCallback> invoker;

    @Nullable
    private BiConsumer<Identifier, TCallback> registrar;

    public EventMapperImpl(String name) {
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
    public TCallback invoker() {
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
    public void configureMapping(BiConsumer<Identifier, TCallback> registrar, Supplier<TCallback> invoker) {
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
