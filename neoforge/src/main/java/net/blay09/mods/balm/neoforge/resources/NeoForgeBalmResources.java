package net.blay09.mods.balm.neoforge.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.common.resources.ResourceConditionContextImpl;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Objects;

public class NeoForgeBalmResources implements BalmResources {
    @Override
    public void registerResourceCondition(ResourceLocation identifier, MapCodec<BalmResourceCondition> codec) {
        final var register = DeferredRegisters.get(NeoForgeRegistries.CONDITION_SERIALIZERS, identifier.getNamespace());
        register.register(identifier.getPath(), () -> codec.xmap(it -> new ICondition() {
            @Override
            public boolean test(IContext context) {
                return it.test(new ResourceConditionContextImpl(context));
            }

            @Override
            public MapCodec<? extends ICondition> codec() {
                return Objects.requireNonNull(NeoForgeRegistries.CONDITION_SERIALIZERS.getValue(identifier));
            }
        }, it -> (BalmResourceCondition) context -> it.test((ICondition.IContext) context.backingContext())));
    }
}
