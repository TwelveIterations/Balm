package net.blay09.mods.balm.forge.server.packs.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.resources.ForgeBalmResourceCondition;
import net.blay09.mods.balm.server.packs.resources.BalmResourceCondition;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeBalmResourceConditionRegistrar implements BalmResourceConditionRegistrar {

    private final String namespace;

    public ForgeBalmResourceConditionRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public <T extends BalmResourceCondition> void register(String name, MapCodec<T> codec) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var register = DeferredRegisters.get(ForgeRegistries.CONDITION_SERIALIZERS.getKey(), identifier.getNamespace());
        register.register(identifier.getPath(),
                () -> codec.xmap(it -> new ForgeBalmResourceCondition<>(identifier, it, id -> ForgeRegistries.CONDITION_SERIALIZERS.get().getValue(id)),
                        ForgeBalmResourceCondition::delegate));
    }
}
