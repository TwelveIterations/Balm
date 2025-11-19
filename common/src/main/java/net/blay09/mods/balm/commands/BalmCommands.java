package net.blay09.mods.balm.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.balm.Balm;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface BalmCommands {
    static void registerPermission(Identifier identifier, Permission permission) {
        Balm.permissions().registerPermission(identifier,
                (context) -> context.getCommandSource().map(it -> it.permissions().hasPermission(permission)).orElse(false));
    }

    static Predicate<CommandSourceStack> requirePermission(Identifier identifier) {
        return (source) -> Balm.permissions().hasPermission(source, identifier);
    }

    static Predicate<CommandSourceStack> requireAnyPermission(Identifier... identifiers) {
        return (source) -> Arrays.stream(identifiers).anyMatch(it -> Balm.permissions().hasPermission(source, it));
    }

    static Predicate<CommandSourceStack> requireAllPermissions(Identifier... identifiers) {
        return (source) -> Arrays.stream(identifiers).allMatch(it -> Balm.permissions().hasPermission(source, it));
    }

    void register(Consumer<CommandDispatcher<CommandSourceStack>> initializer);
}
