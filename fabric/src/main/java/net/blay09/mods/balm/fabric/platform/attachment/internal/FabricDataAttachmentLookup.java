package net.blay09.mods.balm.fabric.platform.attachment.internal;

import net.blay09.mods.balm.platform.attachment.DataAttachmentLookup;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("UnstableApiUsage")
public class FabricDataAttachmentLookup<T> implements DataAttachmentLookup<T> {

    private final AttachmentType<T> type;

    public FabricDataAttachmentLookup(AttachmentType<T> type) {
        this.type = type;
    }

    @Override
    public T get(Player player) {
        return player.getAttached(type);
    }
}
