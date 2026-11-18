package net.blay09.mods.balm.world.item;

import java.util.Map;

public interface BalmDiscriminatedItemRegistration<T> extends Map<T, BalmItemRegistration> {
    DiscriminatedItems<T> asDiscriminatedItems();
}
