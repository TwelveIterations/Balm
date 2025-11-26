package net.blay09.mods.balm.forge.client.color.item;

import net.blay09.mods.balm.client.color.item.BalmItemColorRegistrar;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

import java.util.function.Supplier;

public class ForgeBalmItemColorRegistrar implements BalmItemColorRegistrar {
    private final RegisterColorHandlersEvent.Item event;

    public ForgeBalmItemColorRegistrar(RegisterColorHandlersEvent.Item event) {
        this.event = event;
    }

    @Override
    public void register(ItemColor color, Supplier<ItemLike[]> items) {
        event.register(color, items.get());
    }
}
