package net.blay09.mods.balm.mixin;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Accessor("children")
    List<GuiEventListener> balm_getChildren();

    @Accessor("narratables")
    List<NarratableEntry> balm_getNarratables();

    @Accessor("renderables")
    List<Renderable> balm_getRenderables();
}
