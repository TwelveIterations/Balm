package net.blay09.mods.balm.client.platform.internal;

import net.blay09.mods.balm.client.platform.BalmClientHooks;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jspecify.annotations.Nullable;

public class CommonBalmClientHooks implements BalmClientHooks {

    /**
     * Returns true if an element on the screen holds the focus.
     * @return true if any element on the screen is focused
     */
    @Override
    public boolean hasFocusedElement(ContainerEventHandler parent) {
        return getFocusedElement(parent) != null;
    }

    /**
     * Finds the focused child element on the screen, if any.
     *
     * @return The deepest focused child element, or {@code null} if no child is focused.
     */
    @Override
    public @Nullable GuiEventListener getFocusedElement(ContainerEventHandler parent) {
        final var focused = parent.getFocused();
        if (focused instanceof ContainerEventHandler focusedParent) {
            final var focusedChild = getFocusedElement(focusedParent);
            return focusedChild != null ? focusedChild : focused;
        } else if (focused != null) {
            return focused;
        }

        for (final var child : parent.children()) {
            if (child instanceof ContainerEventHandler childParent) {
                final var focusedChild = getFocusedElement(childParent);
                if (focusedChild != null) {
                    return focusedChild;
                }
            }

            if (child.isFocused()) {
                return child;
            }
        }

        return null;
    }

}
