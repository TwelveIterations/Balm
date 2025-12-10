package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.minecraftforge.eventbus.api.bus.BusGroup;

public record ForgeLoadContext(BusGroup modBusGroup) implements BalmRuntimeLoadContext {
}
