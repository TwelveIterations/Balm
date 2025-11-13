package net.blay09.mods.balm.client.event.callback;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.event.EventHandling;
import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public interface RenderCallback {
    @FunctionalInterface
    interface UpdateFov {
        float handle(LivingEntity entity, float fov);

        EventMapper<UpdateFov> EVENT = EventMapper.createUnbound("RenderCallback.UpdateFov");
    }

    @FunctionalInterface
    interface BlockHighlight {
        EventHandling handle(BlockHitResult hitResult, PoseStack poseStack, MultiBufferSource multiBufferSource, Camera camera);

        EventMapper<BlockHighlight> EVENT = EventMapper.createUnbound("RenderCallback.BlockHighlight");
    }

    @FunctionalInterface
    interface Hand {
        void handle(InteractionHand hand, ItemStack itemStack, float swingProgress);

        EventMapper<Hand> EVENT = EventMapper.createUnbound("RenderCallback.Hand");
    }

    @FunctionalInterface
    interface Gui {
        EventHandling handle(GuiGraphics guiGraphics, Window window);

        @FunctionalInterface
        interface Health extends Gui {
            EventMapper<Health> PRE = EventMapper.createUnbound("RenderCallback.Gui.Health.PRE");
            EventMapper<Health> POST = EventMapper.createUnbound("RenderCallback.Gui.Health.POST");
        }

        @FunctionalInterface
        interface Chat extends Gui {
            EventMapper<Chat> PRE = EventMapper.createUnbound("RenderCallback.Gui.Chat.PRE");
            EventMapper<Chat> POST = EventMapper.createUnbound("RenderCallback.Gui.Chat.POST");
        }

        @FunctionalInterface
        interface Debug extends Gui {
            EventMapper<Debug> PRE = EventMapper.createUnbound("RenderCallback.Gui.Debug.PRE");
            EventMapper<Debug> POST = EventMapper.createUnbound("RenderCallback.Gui.Debug.POST");
        }

        @FunctionalInterface
        interface BossInfo extends Gui {
            EventMapper<BossInfo> PRE = EventMapper.createUnbound("RenderCallback.Gui.BossInfo.PRE");
            EventMapper<BossInfo> POST = EventMapper.createUnbound("RenderCallback.Gui.BossInfo.POST");
        }

        @FunctionalInterface
        interface PlayerList extends Gui {
            EventMapper<PlayerList> PRE = EventMapper.createUnbound("RenderCallback.Gui.PlayerList.PRE");
            EventMapper<PlayerList> POST = EventMapper.createUnbound("RenderCallback.Gui.PlayerList.POST");
        }

        EventMapper<Gui> PRE = EventMapper.createUnbound("RenderCallback.Gui.PRE");
        EventMapper<Gui> POST = EventMapper.createUnbound("RenderCallback.Gui.POST");
    }
}
