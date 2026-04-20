package net.blay09.mods.balm.client.platform;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jspecify.annotations.Nullable;

public interface BalmClientHooks {

    /**
     * Finds the focused child element on the screen, if any.
     *
     * @param parent the screen to check for focused elements
     * @return The deepest focused child element, or {@code null} if no child is focused.
     */
    @Nullable
    GuiEventListener getFocusedElement(ContainerEventHandler screen);

    /**
     * Returns true if an element on the screen holds the focus.
     *
     * @param parent the screen to check for focused elements
     * @return true if any element on the screen is focused
     */
    boolean hasFocusedElement(ContainerEventHandler parent);
}
