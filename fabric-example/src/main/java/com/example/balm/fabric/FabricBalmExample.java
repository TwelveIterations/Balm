package com.example.balm.fabric;

import com.example.balm.BalmExample;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.fabricmc.api.ModInitializer;

public class FabricBalmExample implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initializeMod("balm_example", FabricLoadContext.INSTANCE, BalmExample::initialize);
    }
}
