package net.blay09.mods.balm.client.platform.config;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface BalmConfigScreenProvider {
    @Nullable
    BalmConfigScreenFactory factory(String modId);
}
