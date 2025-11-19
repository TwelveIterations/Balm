package net.blay09.mods.balm.neoforge.platform.runtime;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.neoforged.bus.api.IEventBus;

public record NeoForgeLoadContext(IEventBus modBus) implements BalmRuntimeLoadContext {
}
