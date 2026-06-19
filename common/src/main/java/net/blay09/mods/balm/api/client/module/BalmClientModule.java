package net.blay09.mods.balm.api.client.module;

import net.blay09.mods.balm.api.client.commands.BalmClientCommands;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.minecraft.resources.ResourceLocation;

public interface BalmClientModule {
    ResourceLocation getId();

    default void registerEvents(BalmEvents events) {
    }

    default void registerModels(BalmModels models) {
    }

    default void registerRenderers(BalmRenderers renderers) {
    }

    default void registerScreens(BalmScreens screens) {
    }

    default void registerKeyMappings(BalmKeyMappings keyMappings) {
    }

    default void initialize() {
    }

    default void registerClientCommands(BalmClientCommands commands) {
    }
}
