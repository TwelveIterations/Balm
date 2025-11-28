package net.blay09.mods.balm.fabric.platform.attachment.internal;

import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistration;
import net.blay09.mods.balm.platform.attachment.DataAttachmentLookup;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

@SuppressWarnings("UnstableApiUsage")
public class FabricBalmDataAttachmentTypeRegistration<T> implements BalmDataAttachmentTypeRegistration<T> {
    private final DataAttachmentLookup<T> lookup;

    public FabricBalmDataAttachmentTypeRegistration(AttachmentType<T> type) {
        this.lookup = new FabricDataAttachmentLookup<>(type);
    }

    @Override
    public DataAttachmentLookup<T> asLookup() {
        return lookup;
    }
}
