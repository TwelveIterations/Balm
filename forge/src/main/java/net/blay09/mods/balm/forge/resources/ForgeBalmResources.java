package net.blay09.mods.balm.forge.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeBalmResources implements BalmResources {
    @Override
    public <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        final var register = DeferredRegisters.get(ForgeRegistries.CONDITION_SERIALIZERS.getKey(), identifier.getNamespace());
        register.register(identifier.getPath(),
                () -> codec.xmap(it -> new ForgeBalmResourceCondition<>(identifier, it, id -> ForgeRegistries.CONDITION_SERIALIZERS.get().getValue(id)),
                        ForgeBalmResourceCondition::delegate));
    }
}
