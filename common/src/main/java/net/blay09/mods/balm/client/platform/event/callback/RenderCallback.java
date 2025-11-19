package net.blay09.mods.balm.client.platform.event.callback;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.platform.event.EventHandling;
import net.blay09.mods.balm.platform.event.EventMapper;
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
        EventHandling handle(InteractionHand hand, ItemStack itemStack, float swingProgress);

        EventMapper<Hand> EVENT = EventMapper.createUnbound("RenderCallback.RenderHand");
    }

    @FunctionalInterface
    interface Gui {
        EventHandling handle(GuiGraphics guiGraphics, Window window);

        @FunctionalInterface
        interface Health extends Gui {
            EventMapper<Health> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Health.Before");
            EventMapper<Health> AFTER = EventMapper.createUnbound("RenderCallback.Gui.Health.After");
        }

        @FunctionalInterface
        interface Chat extends Gui {
            EventMapper<Chat> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Chat.Before");
            EventMapper<Chat> AFTER = EventMapper.createUnbound("RenderCallback.Gui.Chat.After");
        }

        @FunctionalInterface
        interface Debug extends Gui {
            EventMapper<Debug> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Debug.Before");
            EventMapper<Debug> AFTER = EventMapper.createUnbound("RenderCallback.Gui.Debug.After");
        }

        @FunctionalInterface
        interface BossInfo extends Gui {
            EventMapper<BossInfo> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.BossInfo.Before");
            EventMapper<BossInfo> AFTER = EventMapper.createUnbound("RenderCallback.Gui.BossInfo.After");
        }

        @FunctionalInterface
        interface PlayerList extends Gui {
            EventMapper<PlayerList> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.PlayerList.Before");
            EventMapper<PlayerList> AFTER = EventMapper.createUnbound("RenderCallback.Gui.PlayerList.After");
        }

        EventMapper<Gui> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Before");
        EventMapper<Gui> AFTER = EventMapper.createUnbound("RenderCallback.Gui.After");
    }
}
