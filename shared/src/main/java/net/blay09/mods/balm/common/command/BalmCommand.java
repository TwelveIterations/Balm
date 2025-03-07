package net.blay09.mods.balm.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.common.client.IconExport;
import net.blay09.mods.balm.common.config.ConfigJsonExport;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;

import java.io.File;

public class BalmCommand {

    private static final ResourceLocation PERMISSION_BALM_DEV = new ResourceLocation("balm", "command.balm.dev");
    private static final ResourceLocation PERMISSION_BALM_EXPORT_CONFIG = new ResourceLocation("balm", "command.balm.export.config");
    private static final ResourceLocation PERMISSION_BALM_EXPORT_ICONS = new ResourceLocation("balm", "command.balm.export.icons");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        BalmCommands.registerPermission(PERMISSION_BALM_DEV, 2);
        BalmCommands.registerPermission(PERMISSION_BALM_EXPORT_CONFIG, 4);
        BalmCommands.registerPermission(PERMISSION_BALM_EXPORT_ICONS, 4);

        dispatcher.register(Commands.literal("balm")
                .then(Commands.literal("dev")
                        .requires(BalmCommands.requirePermission(PERMISSION_BALM_DEV))
                        .executes(context -> {
                            final var source = context.getSource();
                            final var server = source.getServer();
                            final var gameRules = server.getGameRules();
                            gameRules.getRule(GameRules.RULE_DAYLIGHT).set(false, server);
                            source.sendSuccess(() -> Component.literal("Daylight cycle disabled"), true);
                            gameRules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
                            source.sendSuccess(() -> Component.literal("Weather cycle disabled"), true);
                            gameRules.getRule(GameRules.RULE_KEEPINVENTORY).set(true, server);
                            source.sendSuccess(() -> Component.literal("Keep Inventory enabled"), true);
                            gameRules.getRule(GameRules.RULE_DOINSOMNIA).set(false, server);
                            source.sendSuccess(() -> Component.literal("Insomnia disabled"), true);
                            gameRules.getRule(GameRules.RULE_MOBGRIEFING).set(false, server);
                            source.sendSuccess(() -> Component.literal("Mob Griefing disabled"), true);
                            gameRules.getRule(GameRules.RULE_DO_TRADER_SPAWNING).set(false, server);
                            source.sendSuccess(() -> Component.literal("Trader Spawning disabled"), true);
                            server.setDifficulty(Difficulty.PEACEFUL, true);
                            source.sendSuccess(() -> Component.literal("Difficulty set to Peaceful"), true);
                            server.overworld().setWeatherParameters(99999, 0, false, false);
                            source.sendSuccess(() -> Component.literal("Weather cleared"), true);
                            for (final var level : server.getAllLevels()) {
                                level.setDayTime(1000);
                            }
                            source.sendSuccess(() -> Component.literal("Set the time to Daytime"), true);
                            return 0;
                        }))
                .then(Commands.literal("export")
                        .requires(BalmCommands.requireAnyPermission(PERMISSION_BALM_EXPORT_CONFIG, PERMISSION_BALM_EXPORT_ICONS))
                        .then(Commands.literal("config")
                                .requires(BalmCommands.requirePermission(PERMISSION_BALM_EXPORT_CONFIG))
                                .then(Commands.argument("class", StringArgumentType.greedyString()).executes(context -> {
                                            final var className = context.getArgument("class", String.class);
                                            try {
                                                final var configDataClass = Class.forName(className);
                                                ConfigJsonExport.exportToFile(configDataClass, new File("exports/config/" + configDataClass.getSimpleName() + ".json"));
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                                throw new RuntimeException("Invalid config data class: " + className, e);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                throw new RuntimeException("Error exporting config data class: " + className, e);
                                            }

                                            context.getSource().sendSuccess(() -> Component.literal("Exported config for " + className), false);
                                            return 0;
                                        })
                                )).then(Commands.literal("icons")
                                .requires(BalmCommands.requirePermission(PERMISSION_BALM_EXPORT_ICONS))
                                .then(Commands.argument("filter", StringArgumentType.greedyString()).executes(context -> {
                                    final var filter = context.getArgument("filter", String.class);
                                    if (Balm.getProxy().isClient()) {
                                        try {
                                            IconExport.export(filter);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            throw new RuntimeException("Error exporting icons for " + filter, e);
                                        }
                                        context.getSource().sendSuccess(() -> Component.literal("Exported icons for " + filter), false);
                                        return 1;
                                    }
                                    return 0;
                                })))));
    }
}
