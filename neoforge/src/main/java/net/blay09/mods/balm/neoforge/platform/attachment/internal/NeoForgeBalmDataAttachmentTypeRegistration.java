package net.blay09.mods.balm.neoforge.platform.attachment.internal;

import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistration;
import net.blay09.mods.balm.platform.attachment.DataAttachmentLookup;
import net.minecraft.core.Holder;

public class NeoForgeBalmDataAttachmentTypeRegistration<T> implements BalmDataAttachmentTypeRegistration<T> {
    private final DataAttachmentLookup<T> lookup;

    public NeoForgeBalmDataAttachmentTypeRegistration(Holder<?> type) {
        this.lookup = new NeoForgeDataAttachmentLookup<>(type);
    }

    @Override
    public DataAttachmentLookup<T> asLookup() {
        return lookup;
    }
}
