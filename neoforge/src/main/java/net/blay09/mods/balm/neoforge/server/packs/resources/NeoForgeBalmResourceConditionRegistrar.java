package net.blay09.mods.balm.neoforge.server.packs.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.blay09.mods.balm.neoforge.resources.NeoForgeBalmResourceCondition;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NeoForgeBalmResourceConditionRegistrar implements BalmResourceConditionRegistrar {

    private final String namespace;

    public NeoForgeBalmResourceConditionRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public <T extends BalmResourceCondition> void register(String name, MapCodec<T> codec) {
        final var identifier = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var register = DeferredRegisters.get(NeoForgeRegistries.CONDITION_SERIALIZERS, identifier.getNamespace());
        register.register(identifier.getPath(),
                () -> codec.xmap(it -> new NeoForgeBalmResourceCondition<>(identifier, it, NeoForgeRegistries.CONDITION_SERIALIZERS::getValue),
                        NeoForgeBalmResourceCondition::delegate));
    }
}
