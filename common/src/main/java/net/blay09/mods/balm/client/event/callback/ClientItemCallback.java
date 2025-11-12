package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ClientItemCallback {

    @FunctionalInterface
    interface Use {
        void handle(InteractionHand hand);

        EventMapper<Use> EVENT = EventMapper.createUnbound();
    }

}
