package net.blay09.mods.balm.client.event.callback;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
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

        EventMapper<UpdateFov> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface BlockHighlight {
        void handle(BlockHitResult hitResult, PoseStack poseStack, MultiBufferSource multiBufferSource, Camera camera);

        EventMapper<BlockHighlight> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Hand {
        void handle(InteractionHand hand, ItemStack itemStack, float swingProgress);

        EventMapper<Hand> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Gui {
        void handle(GuiGraphics guiGraphics, Window window);

        @FunctionalInterface
        interface Health extends Gui {
            EventMapper<Health> PRE = EventMapper.createUnbound();
            EventMapper<Health> POST = EventMapper.createUnbound();
        }

        @FunctionalInterface
        interface Chat extends Gui {
            EventMapper<Chat> PRE = EventMapper.createUnbound();
            EventMapper<Chat> POST = EventMapper.createUnbound();
        }

        @FunctionalInterface
        interface Debug extends Gui {
            EventMapper<Debug> PRE = EventMapper.createUnbound();
            EventMapper<Debug> POST = EventMapper.createUnbound();
        }

        @FunctionalInterface
        interface BossInfo extends Gui {
            EventMapper<BossInfo> PRE = EventMapper.createUnbound();
            EventMapper<BossInfo> POST = EventMapper.createUnbound();
        }

        @FunctionalInterface
        interface PlayerList extends Gui {
            EventMapper<PlayerList> PRE = EventMapper.createUnbound();
            EventMapper<PlayerList> POST = EventMapper.createUnbound();
        }

        EventMapper<Gui> PRE = EventMapper.createUnbound();
        EventMapper<Gui> POST = EventMapper.createUnbound();
    }
}
