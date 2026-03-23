package net.blay09.mods.balm.fabric.server.packs.resources.internal;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.server.packs.resources.BalmResourceCondition;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricBalmResourceConditionRegistrar implements BalmResourceConditionRegistrar {

    private static final Map<Identifier, ResourceConditionType<?>> conditions = new ConcurrentHashMap<>();

    private final String namespace;

    public FabricBalmResourceConditionRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public <T extends BalmResourceCondition> void register(String path, MapCodec<T> codec) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, path);
        final var type = ResourceConditionType.create(identifier, codec
                .xmap(it -> new FabricBalmResourceCondition<>(identifier, it, conditions::get),
                        FabricBalmResourceCondition::delegate));
        ResourceConditions.register(type);
        conditions.put(identifier, type);
    }
}
