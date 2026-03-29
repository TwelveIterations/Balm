package com.example.balm.neoforge;

import com.example.balm.BalmExample;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = "balm_example")
public class NeoForgeBalmExample {

    public NeoForgeBalmExample(ModContainer modContainer, IEventBus modEventBus) {
        Balm.initializeMod("balm_example", new NeoForgeLoadContext(modContainer, modEventBus), BalmExample::initialize);
    }
}
