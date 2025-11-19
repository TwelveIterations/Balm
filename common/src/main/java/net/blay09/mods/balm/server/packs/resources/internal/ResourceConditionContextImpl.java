package net.blay09.mods.balm.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.ResourceConditionContext;
import org.jspecify.annotations.Nullable;

public record ResourceConditionContextImpl(@Nullable Object backingContext) implements ResourceConditionContext {
}
