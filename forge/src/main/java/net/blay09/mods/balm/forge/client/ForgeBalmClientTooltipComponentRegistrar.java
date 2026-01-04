package net.blay09.mods.balm.forge.client.internal;

import net.blay09.mods.balm.client.BalmClientTooltipComponentRegistrar;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

import java.util.function.Function;

public class ForgeBalmClientTooltipComponentRegistrar implements BalmClientTooltipComponentRegistrar {

    private final RegisterClientTooltipComponentFactoriesEvent event;

    public ForgeBalmClientTooltipComponentRegistrar(RegisterClientTooltipComponentFactoriesEvent event) {
        this.event = event;
    }

    @Override
    public <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {
        event.register(type, factory);
    }
}
