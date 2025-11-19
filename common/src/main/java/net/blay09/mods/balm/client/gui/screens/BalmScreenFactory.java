package net.blay09.mods.balm.client.gui.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface BalmScreenFactory<T, S> {
    S create(T menu, Inventory inventory, Component title);
}
