package net.blay09.mods.balm.neoforge.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NeoForgeBalmResources implements BalmResources {
    @Override
    public <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        final var register = DeferredRegisters.get(NeoForgeRegistries.CONDITION_SERIALIZERS, identifier.getNamespace());
        register.register(identifier.getPath(),
                () -> codec.xmap(it -> new NeoForgeBalmResourceCondition<>(identifier, it, NeoForgeRegistries.CONDITION_SERIALIZERS::getValue),
                        NeoForgeBalmResourceCondition::delegate));
    }
}
