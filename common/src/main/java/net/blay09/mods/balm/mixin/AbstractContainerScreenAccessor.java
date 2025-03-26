package net.blay09.mods.balm.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {

    @Accessor
    int getLeftPos();

    @Accessor
    int getTopPos();

    @Accessor
    int getImageWidth();

    @Accessor
    int getImageHeight();

    @Accessor
    Slot getHoveredSlot();

    @Invoker
    boolean callIsHovering(Slot slot, double mouseX, double mouseY);

    @Invoker
    void callRenderSlot(GuiGraphics guiGraphics, Slot slot);
}
