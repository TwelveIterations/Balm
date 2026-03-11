package net.blay09.mods.balm.client.platform.event.callback;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public interface RenderCallback {
    @FunctionalInterface
    interface UpdateFov {
        float computeFov(LivingEntity entity, float fov);

        EventMapper<UpdateFov> EVENT = EventMapper.createUnbound("RenderCallback.UpdateFov");
    }

    @FunctionalInterface
    interface BlockHighlight {
        boolean shouldRender(BlockHitResult hitResult, PoseStack poseStack, MultiBufferSource multiBufferSource, Camera camera, int color, float lineWidth);

        EventMapper<BlockHighlight> EVENT = EventMapper.createUnbound("RenderCallback.BlockHighlight");
    }

    @FunctionalInterface
    interface Hand {
        boolean shouldRender(InteractionHand hand, ItemStack itemStack, float swingProgress);

        EventMapper<Hand> EVENT = EventMapper.createUnbound("RenderCallback.RenderHand");
    }

    interface Gui {

        @FunctionalInterface
        interface Before {
            boolean shouldRender(GuiGraphicsExtractor guiGraphics, Window window);
        }

        @FunctionalInterface
        interface After {
            void afterRender(GuiGraphicsExtractor guiGraphics, Window window);
        }

        interface Health {
            EventMapper<Before> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Health.Before");
            EventMapper<After> AFTER = EventMapper.createUnbound("RenderCallback.Gui.Health.After");
        }

        interface Chat {
            EventMapper<Before> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Chat.Before");
            EventMapper<After> AFTER = EventMapper.createUnbound("RenderCallback.Gui.Chat.After");
        }

        interface Debug {
            EventMapper<Before> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Debug.Before");
            EventMapper<After> AFTER = EventMapper.createUnbound("RenderCallback.Gui.Debug.After");
        }

        interface BossInfo {
            EventMapper<Before> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.BossInfo.Before");
            EventMapper<After> AFTER = EventMapper.createUnbound("RenderCallback.Gui.BossInfo.After");
        }

        interface PlayerList {
            EventMapper<Before> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.PlayerList.Before");
            EventMapper<After> AFTER = EventMapper.createUnbound("RenderCallback.Gui.PlayerList.After");
        }

        EventMapper<Before> BEFORE = EventMapper.createUnbound("RenderCallback.Gui.Before");
        EventMapper<After> AFTER = EventMapper.createUnbound("RenderCallback.Gui.After");
    }
}
