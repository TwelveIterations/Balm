package net.blay09.mods.balm.event.internal;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class EventMapperImpl<TCallback, TInvoker> implements EventMapper<TCallback, TInvoker> {

    @Nullable
    private TInvoker invoker;

    @Nullable
    private BiConsumer<ResourceLocation, TCallback> registrar;

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

        return invoker;
    }

    @Override
    public void bind(BiConsumer<ResourceLocation, TCallback> registrar, TInvoker invoker) {
        this.registrar = registrar;
        this.invoker = invoker;
    }

}
