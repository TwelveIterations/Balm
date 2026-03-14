package net.blay09.mods.balm.world.item;

import org.jspecify.annotations.Nullable;

import java.util.Map;

public interface BalmDiscriminatedItemRegistration<T> extends Map<@Nullable T, BalmItemRegistration> {
    DiscriminatedItems<T> asDiscriminatedItems();
}
