package net.blay09.mods.balm.core;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DeferredHolder<T> implements Holder<T> {

    private final ResourceKey<T> resourceKey;
    @Nullable
    private Holder<T> delegate;

    @Nullable
    private T earlyConstructedInstance;

    public DeferredHolder(ResourceKey<T> resourceKey) {
        this.resourceKey = resourceKey;
    }

    public DeferredHolder(ResourceKey<T> resourceKey, @Nullable T earlyConstructedInstance) {
        this.resourceKey = resourceKey;
        this.earlyConstructedInstance = earlyConstructedInstance;
    }

    @Override
    public T value() {
        if (earlyConstructedInstance != null) {
            return earlyConstructedInstance;
        }

        tryBind();
        if (delegate == null) {
            throw new IllegalStateException("Tried to access " + resourceKey + " before it was bound");
        }

        return delegate.value();
    }

    @Override
    public boolean isBound() {
        tryBind();
        return delegate != null && delegate.isBound();
    }

    @Override
    public boolean is(ResourceLocation resourceLocation) {
        return resourceKey.location().equals(resourceLocation);
    }

    @Override
    public boolean is(ResourceKey<T> resourceKey) {
        return this.resourceKey.equals(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return predicate.test(resourceKey);
    }

    @Override
    public boolean is(TagKey<T> tagKey) {
        tryBind();
        return delegate != null && delegate.is(tagKey);
    }

    @Override
    public boolean is(Holder<T> holder) {
        tryBind();
        return delegate != null ? delegate.is(holder) : equals(holder);
    }

    @Override
    public Stream<TagKey<T>> tags() {
        tryBind();
        return delegate != null ? delegate.tags() : Stream.empty();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return Either.left(resourceKey);
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.of(resourceKey);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> holderOwner) {
        tryBind();
        return delegate != null && delegate.canSerializeIn(holderOwner);
    }

    @SuppressWarnings("unchecked")
    private void tryBind() {
        if (delegate == null) {
            final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(resourceKey.registry());
            if (registry != null) {
                delegate = registry.get(resourceKey).orElse(null);
            }
        }
    }
}
