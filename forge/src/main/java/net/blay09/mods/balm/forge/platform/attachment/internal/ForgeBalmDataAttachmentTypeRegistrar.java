package net.blay09.mods.balm.forge.platform.attachment.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistration;
import net.blay09.mods.balm.platform.attachment.DataAttachmentTypeBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public class ForgeBalmDataAttachmentTypeRegistrar implements BalmDataAttachmentTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public ForgeBalmDataAttachmentTypeRegistrar(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <T> BalmDataAttachmentTypeRegistration<T> register(String name, BiFunction<ResourceLocation, DataAttachmentTypeBuilder<T>, DataAttachmentTypeBuilder<T>> constructor) {
        throw new UnsupportedOperationException("Data attachments are not yet supported on Forge.");
    }

}
