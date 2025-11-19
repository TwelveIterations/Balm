package net.blay09.mods.balm.api;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.minecraft.core.Holder}, {@link net.minecraft.resources.ResourceKey} or the actual object instead via {@link net.blay09.mods.balm.core.BalmHolderRegistration}.
 */
@Deprecated
public class DeferredObject<T> {
    private final Identifier id;
    private final Supplier<T> supplier;
    private final Supplier<Boolean> canResolveFunc;
    protected T object;

    protected DeferredObject(Identifier id) {
        this(id, () -> null, () -> false);
    }

    public DeferredObject(Identifier id, Supplier<T> supplier) {
        this(id, supplier, () -> false);
    }

    public DeferredObject(Identifier id, Supplier<T> supplier, Supplier<Boolean> canResolveFunc) {
        this.id = id;
        this.supplier = supplier;
        this.canResolveFunc = canResolveFunc;
    }

    public static <T> DeferredObject<T> of(Identifier identifier, T instance) {
        return new DeferredObject<>(identifier, () -> instance).resolveImmediately();
    }

    protected void set(T object) {
        this.object = object;
    }

    public boolean canResolve() {
        return canResolveFunc.get();
    }

    public T resolve() {
        if (object == null) {
            object = supplier.get();
        }
        return object;
    }

    public T get() {
        if (object == null) {
            if (canResolve()) {
                return resolve();
            }

            throw new IllegalStateException("Tried to access deferred object before it was resolved.");
        }

        return object;
    }

    public DeferredObject<T> resolveImmediately() {
        resolve();
        return this;
    }

    public Identifier getIdentifier() {
        return id;
    }

}
