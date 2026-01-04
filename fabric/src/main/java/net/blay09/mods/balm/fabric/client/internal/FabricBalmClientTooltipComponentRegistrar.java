package net.blay09.mods.balm.fabric.client.internal;

import net.blay09.mods.balm.client.BalmClientTooltipComponentRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.function.Function;

public class FabricBalmClientTooltipComponentRegistrar implements BalmClientTooltipComponentRegistrar {

    public static final FabricBalmClientTooltipComponentRegistrar INSTANCE = new FabricBalmClientTooltipComponentRegistrar();

    @Override
    public <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {
        TooltipComponentCallback.EVENT.register(data -> {
            if (data.getClass() == type) {
                return factory.apply(type.cast(data));
            }
            return null;
        });
    }
}
