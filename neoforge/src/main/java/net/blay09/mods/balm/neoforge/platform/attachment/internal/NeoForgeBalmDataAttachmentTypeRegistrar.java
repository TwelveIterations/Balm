package net.blay09.mods.balm.neoforge.platform.attachment.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistration;
import net.blay09.mods.balm.platform.attachment.DataAttachmentTypeBuilder;
import net.blay09.mods.balm.platform.attachment.internal.DataAttachmentTypeBuilderImpl;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.BiFunction;

public class NeoForgeBalmDataAttachmentTypeRegistrar implements BalmDataAttachmentTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public NeoForgeBalmDataAttachmentTypeRegistrar(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <T> BalmDataAttachmentTypeRegistration<T> register(String name, BiFunction<Identifier, DataAttachmentTypeBuilder<T>, DataAttachmentTypeBuilder<T>> constructor) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var builder = (DataAttachmentTypeBuilderImpl<T>) constructor.apply(identifier, new DataAttachmentTypeBuilderImpl<>());
        final var neoForgeBuilder = AttachmentType.builder(builder.getInitializer() != null ? builder.getInitializer() : () -> null);
        if (builder.getPersistentCodec() != null) {
            neoForgeBuilder.serialize(builder.getPersistentCodec().fieldOf("value"));
        }
        if (builder.getStreamCodec() != null) {
            if (builder.getSyncPredicate() != null) {
                neoForgeBuilder.sync((holder, to) -> builder.getSyncPredicate().test(holder, to), builder.getStreamCodec());
            } else {
                neoForgeBuilder.sync(builder.getStreamCodec());
            }
        }
        if (builder.isCopyOnDeath()) {
            neoForgeBuilder.copyOnDeath();
        }
        final var resourceKey = ResourceKey.create(NeoForgeRegistries.ATTACHMENT_TYPES.key(), identifier);
        final var holder = registrar.register(resourceKey, (ignored) -> neoForgeBuilder.build());
        return new NeoForgeBalmDataAttachmentTypeRegistration<>(holder.asHolder());
    }

}
