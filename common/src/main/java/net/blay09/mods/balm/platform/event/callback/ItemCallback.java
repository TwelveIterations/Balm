package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ItemCallback {

    @FunctionalInterface
    interface Use {
        InteractionResult handle(Player player, Level level, InteractionHand hand);

        EventMapper<Use> EVENT = EventMapper.createUnbound("ItemCallback.Use");
    }

    @FunctionalInterface
    interface Tooltip {
        void handle(ItemStack itemStack, List<Component> tooltip, TooltipFlag flags);

        EventMapper<Tooltip> EVENT = EventMapper.createUnbound("ItemCallback.Tooltip");
    }

    interface Craft {
        @FunctionalInterface
        interface After {
            void afterCraft(Player player, ItemStack itemStack, Container craftMatrix);

            EventMapper<After> EVENT = EventMapper.createUnbound("ItemCallback.Craft.After");
        }
    }

    interface Toss {
        @FunctionalInterface
        interface Before {
            boolean allowToss(Player player, ItemStack itemStack);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ItemCallback.Toss.Before");
        }

    }

}
