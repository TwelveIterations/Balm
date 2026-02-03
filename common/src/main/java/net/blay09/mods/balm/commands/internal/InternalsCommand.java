package net.blay09.mods.balm.commands.internal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.commands.BalmCommands;
import net.blay09.mods.balm.client.platform.util.IconExport;
import net.blay09.mods.balm.platform.config.util.ConfigJsonExport;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.gamerules.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Registers the <code>/balm</code> commands for developers.
 *
 * @see Balm#commands()
 */
public final class InternalsCommand {

    private static final Logger logger = LoggerFactory.getLogger(InternalsCommand.class);

    private static final Identifier PERMISSION_BALM_DEV = Identifier.fromNamespaceAndPath("balm", "command.balm.dev");
    private static final Identifier PERMISSION_BALM_EXPORT_CONFIG = Identifier.fromNamespaceAndPath("balm", "command.balm.export.config");
    private static final Identifier PERMISSION_BALM_EXPORT_ICONS = Identifier.fromNamespaceAndPath("balm", "command.balm.export.icons");
    private static int balmDevCounter;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        BalmCommands.registerPermission(PERMISSION_BALM_DEV, Permissions.COMMANDS_OWNER);
        BalmCommands.registerPermission(PERMISSION_BALM_EXPORT_CONFIG, Permissions.COMMANDS_OWNER);
        BalmCommands.registerPermission(PERMISSION_BALM_EXPORT_ICONS, Permissions.COMMANDS_OWNER);

        dispatcher.register(Commands.literal("balm")
                .then(Commands.literal("dev")
                        .requires(BalmCommands.requirePermission(PERMISSION_BALM_DEV))
                        .executes(context -> {
                            balmDevCounter++;
                            if (Balm.platform().isDevelopmentEnvironment() || balmDevCounter >= 3) {
                                final var source = context.getSource();
                                final var server = source.getServer();
                                final var level = source.getLevel();
                                final var gameRules = level.getGameRules();
                                gameRules.set(GameRules.ADVANCE_TIME, false, server);
                                source.sendSuccess(() -> Component.literal("Daylight cycle disabled"), true);
                                gameRules.set(GameRules.ADVANCE_WEATHER, false, server);
                                source.sendSuccess(() -> Component.literal("Weather cycle disabled"), true);
                                gameRules.set(GameRules.KEEP_INVENTORY, true, server);
                                source.sendSuccess(() -> Component.literal("Keep Inventory enabled"), true);
                                gameRules.set(GameRules.SPAWN_PHANTOMS, false, server);
                                source.sendSuccess(() -> Component.literal("Insomnia disabled"), true);
                                gameRules.set(GameRules.MOB_GRIEFING, false, server);
                                source.sendSuccess(() -> Component.literal("Mob Griefing disabled"), true);
                                gameRules.set(GameRules.SPAWN_WANDERING_TRADERS, false, server);
                                source.sendSuccess(() -> Component.literal("Trader Spawning disabled"), true);
                                server.setDifficulty(Difficulty.PEACEFUL, true);
                                source.sendSuccess(() -> Component.literal("Difficulty set to Peaceful"), true);
                                final var weatherData = level.getWeatherData();
                                weatherData.setClearWeatherTime(99999);
                                weatherData.setRainTime(0);
                                weatherData.setThunderTime(0);
                                weatherData.setRaining(false);
                                weatherData.setThundering(false);
                                source.sendSuccess(() -> Component.literal("Weather cleared"), true);
                                final var clockManager = source.getServer().clockManager();
                                level.dimensionType().defaultClock().ifPresent(it -> clockManager.setTotalTicks(it, 1000L));
                                source.sendSuccess(() -> Component.literal("Set the time to Daytime"), true);
                            } else {
                                context.getSource()
                                        .sendSuccess(() -> Component.literal(
                                                        "This command will change several game rules and your world's difficulty. You should only use it if you know what you're doing!")
                                                .withStyle(ChatFormatting.RED), true);
                            }
                            return 0;
                        }))
                .then(Commands.literal("export")
                        .requires(BalmCommands.requireAnyPermission(PERMISSION_BALM_EXPORT_CONFIG, PERMISSION_BALM_EXPORT_ICONS))
                        .then(Commands.literal("config")
                                .requires(BalmCommands.requirePermission(PERMISSION_BALM_EXPORT_CONFIG))
                                .then(Commands.argument("mod", StringArgumentType.string()).executes(context -> {
                                            final var mod = context.getArgument("mod", String.class);
                                            final var schemas = Balm.config().getSchemasByNamespace(mod);
                                            try {
                                                ConfigJsonExport.exportToFile(schemas, new File("exports/config/" + mod + ".json"));
                                            } catch (Exception e) {
                                                logger.error("Error exporting config data class", e);
                                                throw new RuntimeException("Error exporting config data class: " + mod, e);
                                            }

                                            context.getSource().sendSuccess(() -> Component.literal("Exported config schema for " + mod), false);
                                            return 0;
                                        })
                                )).then(Commands.literal("icons")
                                .requires(BalmCommands.requirePermission(PERMISSION_BALM_EXPORT_ICONS))
                                .then(Commands.argument("filter", StringArgumentType.greedyString()).executes(context -> {
                                    final var filter = context.getArgument("filter", String.class);
                                    if (Balm.safeClientAccess().isClient()) {
                                        try {
                                            IconExport.export(filter);
                                        } catch (Exception e) {
                                            logger.error("Error exporting icons", e);
                                            throw new RuntimeException("Error exporting icons for " + filter, e);
                                        }
                                        context.getSource().sendSuccess(() -> Component.literal("Exported icons for " + filter), false);
                                        return 1;
                                    }
                                    return 0;
                                })))));
    }
}
