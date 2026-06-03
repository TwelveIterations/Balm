package net.blay09.mods.balm.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.resources.Identifier;

public interface BalmArgumentTypeRegistrar {
    <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void register(Identifier identifier, Class<A> argumentClass, ArgumentTypeInfo<A, T> argumentTypeInfo);

    <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void register(String name, Class<A> argumentClass, ArgumentTypeInfo<A, T> argumentTypeInfo);
}
