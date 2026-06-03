package net.blay09.mods.balm.neoforge.command;

import com.mojang.brigadier.arguments.ArgumentType;
import net.blay09.mods.balm.api.command.BalmArgumentTypeRegistrar;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class NeoForgeBalmArgumentTypeRegistrar implements BalmArgumentTypeRegistrar {
    private final String namespace;

    public NeoForgeBalmArgumentTypeRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void register(ResourceLocation identifier, Class<A> argumentClass, ArgumentTypeInfo<A, T> argumentTypeInfo) {
        DeferredRegisters.get(Registries.COMMAND_ARGUMENT_TYPE, identifier.getNamespace()).register(identifier.getPath(), () -> argumentTypeInfo);
        ArgumentTypeInfos.registerByClass(argumentClass, argumentTypeInfo);
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void register(String name, Class<A> argumentClass, ArgumentTypeInfo<A, T> argumentTypeInfo) {
        register(ResourceLocation.fromNamespaceAndPath(namespace, name), argumentClass, argumentTypeInfo);
    }
}
