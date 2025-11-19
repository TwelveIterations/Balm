package net.blay09.mods.balm.platform.compatibility.hudinfo;

import net.minecraft.network.chat.Component;

public interface HudInfoOutput {
    void text(Component component);

    void progress(float progress);

    default void progress(int progress, int maxProgress) {
        progress(progress / (float) maxProgress);
    }
}
