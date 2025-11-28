package net.blay09.mods.balm.fabric.platform.attachment.internal;

import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistration;
import net.blay09.mods.balm.platform.attachment.DataAttachmentTypeBuilder;
import net.blay09.mods.balm.platform.attachment.internal.DataAttachmentTypeBuilderImpl;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

@SuppressWarnings("UnstableApiUsage")
public class FabricBalmDataAttachmentTypeRegistrar implements BalmDataAttachmentTypeRegistrar {

    private final String namespace;

    public FabricBalmDataAttachmentTypeRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public <T> BalmDataAttachmentTypeRegistration<T> register(String name, BiFunction<ResourceLocation, DataAttachmentTypeBuilder<T>, DataAttachmentTypeBuilder<T>> constructor) {
        final var identifier = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var builder = (DataAttachmentTypeBuilderImpl<T>) constructor.apply(identifier, new DataAttachmentTypeBuilderImpl<>());
        final var type = AttachmentRegistry.<T>create(identifier, fabricBuilder -> {
            if (builder.getInitializer() != null) {
                fabricBuilder.initializer(builder.getInitializer());
            }
            if (builder.getPersistentCodec() != null) {
                fabricBuilder.persistent(builder.getPersistentCodec());
            }
            if (builder.getStreamCodec() != null) {
                if (builder.getSyncPredicate() != null) {
                    fabricBuilder.syncWith(builder.getStreamCodec(), (holder, to) -> builder.getSyncPredicate().test(holder, to));
                } else {
                    fabricBuilder.syncWith(builder.getStreamCodec(), (holder, to) -> true);
                }
            }
            if (builder.isCopyOnDeath()) {
                fabricBuilder.copyOnDeath();
            }
        });
        return new FabricBalmDataAttachmentTypeRegistration<>(type);
    }

}
