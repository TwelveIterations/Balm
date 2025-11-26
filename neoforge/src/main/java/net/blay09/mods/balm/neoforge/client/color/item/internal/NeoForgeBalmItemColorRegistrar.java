package net.blay09.mods.balm.neoforge.client.color.item.internal;

import net.blay09.mods.balm.client.color.item.BalmItemColorRegistrar;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.function.Supplier;

public class NeoForgeBalmItemColorRegistrar implements BalmItemColorRegistrar {
    private final RegisterColorHandlersEvent.Item event;

    public NeoForgeBalmItemColorRegistrar(RegisterColorHandlersEvent.Item event) {
        this.event = event;
    }

    @Override
    public void register(ItemColor color, Supplier<ItemLike[]> items) {
        event.register(color, items.get());
    }
}
