package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTooltipEvent extends BalmEvent {

    private final ItemStack itemStack;
    private final Player player;
    private final List<Component> toolTip;
    private final TooltipFlag flags;

    public ItemTooltipEvent(ItemStack itemStack, Player player, List<Component> toolTip, TooltipFlag flags) {
        this.itemStack = itemStack;
        this.player = player;
        this.toolTip = toolTip;
        this.flags = flags;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public List<Component> getToolTip() {
        return toolTip;
    }

    public TooltipFlag getFlags() {
        return flags;
    }
}
