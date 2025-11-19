package net.blay09.mods.balm.client.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface ClientItemCallback {

    @FunctionalInterface
    interface Use {
        InteractionResult handle(Player player, InteractionHand hand);

        EventMapper<Use> EVENT = EventMapper.createUnbound("ClientItemCallback.Use");
    }

}
