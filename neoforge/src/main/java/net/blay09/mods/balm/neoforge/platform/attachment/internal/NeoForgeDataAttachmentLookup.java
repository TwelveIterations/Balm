package net.blay09.mods.balm.neoforge.platform.attachment.internal;

import net.blay09.mods.balm.platform.attachment.DataAttachmentLookup;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jspecify.annotations.Nullable;

public class NeoForgeDataAttachmentLookup<T> implements DataAttachmentLookup<T> {
    private final Holder<AttachmentType<T>> type;

    @SuppressWarnings("unchecked")
    public NeoForgeDataAttachmentLookup(Holder<?> type) {
        this.type = (Holder<AttachmentType<T>>) type;
    }

    @Override
    public @Nullable T get(Player player) {
        return player.getData(type::value);
    }
}
