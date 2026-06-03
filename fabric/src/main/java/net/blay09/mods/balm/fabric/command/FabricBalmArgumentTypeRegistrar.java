package net.blay09.mods.balm.fabric.command;

import com.mojang.brigadier.arguments.ArgumentType;
import net.blay09.mods.balm.api.command.BalmArgumentTypeRegistrar;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.ResourceLocation;

public class FabricBalmArgumentTypeRegistrar implements BalmArgumentTypeRegistrar {
    private final String namespace;

    public FabricBalmArgumentTypeRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void register(ResourceLocation identifier, Class<A> argumentClass, ArgumentTypeInfo<A, T> argumentTypeInfo) {
        ArgumentTypeRegistry.registerArgumentType(identifier, argumentClass, argumentTypeInfo);
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void register(String name, Class<A> argumentClass, ArgumentTypeInfo<A, T> argumentTypeInfo) {
        register(ResourceLocation.fromNamespaceAndPath(namespace, name), argumentClass, argumentTypeInfo);
    }
}
