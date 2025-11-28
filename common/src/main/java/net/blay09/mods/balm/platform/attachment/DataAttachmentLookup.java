package net.blay09.mods.balm.platform.attachment;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface DataAttachmentLookup<T> {
    @Nullable
    T get(Player player);
}
