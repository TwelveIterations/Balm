package net.blay09.mods.balm.neoforge.platform.runtime;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

public record NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus) implements BalmRuntimeLoadContext {
}
