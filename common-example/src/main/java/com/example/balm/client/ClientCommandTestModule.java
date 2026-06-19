package com.example.balm.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.blay09.mods.balm.client.commands.BalmClientCommands;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ClientCommandTestModule implements BalmClientModule {

    @Override
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath("balm_example", "client_commands");
    }

    @Override
    public void registerClientCommands(BalmClientCommands commands) {
        commands.register(dispatcher -> dispatcher.register(
                LiteralArgumentBuilder.<SharedSuggestionProvider>literal("balm_client_test")
                        .executes(context -> {
                            final var player = Minecraft.getInstance().player;
                            if (player != null) {
                                player.sendSystemMessage(Component.literal("Hello from a Balm client command!"));
                            }
                            return 1;
                        })
        ));
    }
}
