package com.zenith.discord.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.zenith.Proxy;
import com.zenith.discord.command.Command;
import com.zenith.discord.command.CommandContext;
import com.zenith.discord.command.CommandUsage;
import com.zenith.util.spectator.SpectatorEntityRegistry;
import com.zenith.util.spectator.entity.SpectatorEntity;
import discord4j.rest.util.Color;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.zenith.discord.DiscordBot.escape;
import static com.zenith.util.Constants.CONFIG;
import static com.zenith.util.Constants.WHITELIST_MANAGER;
import static java.util.Arrays.asList;

public class SpectatorCommand extends Command {
    @Override
    public CommandUsage commandUsage() {
        return CommandUsage.args(
                "spectator",
                "Configure the Spectator feature",
                asList("on/off",
                        "whitelist add/del <player>", "whitelist list", "whitelist clear",
                        "entity list", "entity <entity>",
                        "chat on/off")
        );
    }

    @Override
    public LiteralArgumentBuilder<CommandContext> register() {
        return command("spectator").requires(Command::validateAccountOwner)
                .then(literal("on").executes(c -> {
                    CONFIG.server.spectator.allowSpectator = true;
                    c.getSource().getEmbedBuilder()
                            .title("Spectators On!")
                            .color(Color.CYAN);
                }))
                .then(literal("off").executes(c -> {
                    CONFIG.server.spectator.allowSpectator = false;
                    Proxy.getInstance().getSpectatorConnections().forEach(connection -> connection.disconnect(CONFIG.server.extra.whitelist.kickmsg));
                    c.getSource().getEmbedBuilder()
                            .title("Spectators Off!")
                            .color(Color.CYAN);
                }))
                .then(literal("whitelist")
                        .then(literal("add").then(argument("player", string()).executes(c -> {
                            final String playerName = StringArgumentType.getString(c, "player");
                            if (WHITELIST_MANAGER.addSpectatorWhitelistEntryByUsername(playerName)) {
                                c.getSource().getEmbedBuilder()
                                        .title("Added user: " + escape(playerName) + " To Spectator Whitelist")
                                        .color(Color.CYAN)
                                        .addField("Spectator Whitelist", whitelistToString(), false);
                            } else {
                                c.getSource().getEmbedBuilder()
                                        .title("Failed to add user: " + escape(playerName) + " to whitelist. Unable to lookup profile.")
                                        .color(Color.RUBY);
                            }
                            return 1;
                        })))
                        .then(literal("del").then(argument("player", string()).executes(c -> {
                            final String playerName = StringArgumentType.getString(c, "player");
                            WHITELIST_MANAGER.removeSpectatorWhitelistEntryByUsername(playerName);
                            c.getSource().getEmbedBuilder()
                                    .title("Removed user: " + escape(playerName) + " From Spectator Whitelist")
                                    .color(Color.CYAN)
                                    .addField("Spectator Whitelist", whitelistToString(), false);
                            return 1;
                        })))
                        .then(literal("clear").executes(c -> {
                            WHITELIST_MANAGER.clearSpectatorWhitelist();
                            c.getSource().getEmbedBuilder()
                                    .title("Spectator Whitelist Cleared")
                                    .color(Color.RUBY)
                                    .addField("Spectator Whitelist", whitelistToString(), false);
                        }))
                        .then(literal("list").executes(c -> {
                            c.getSource().getEmbedBuilder()
                                    .title("Spectator Whitelist")
                                    .color(Color.CYAN)
                                    .addField("Spectator Whitelist", whitelistToString(), false);
                        })))
                .then(literal("entity")
                        .then(literal("list").executes(c -> {
                            c.getSource().getEmbedBuilder()
                                    .title("Entity List")
                                    .addField("Entity", String.join(", ", SpectatorEntityRegistry.getEntityIdentifiers()), false)
                                    .color(Color.CYAN);
                        }))
                        .then(argument("entityID", string()).executes(c -> {
                            final String entityInput = StringArgumentType.getString(c, "entityID");
                            Optional<SpectatorEntity> spectatorEntity = SpectatorEntityRegistry.getSpectatorEntity(entityInput);
                            if (spectatorEntity.isPresent()) {
                                CONFIG.server.spectator.spectatorEntity = entityInput;
                                c.getSource().getEmbedBuilder()
                                        .title("Set Entity")
                                        .addField("Entity", entityInput, false)
                                        .color(Color.CYAN);
                            } else {
                                c.getSource().getEmbedBuilder()
                                        .title("Invalid Entity")
                                        .addField("Valid Entities", String.join(", ", SpectatorEntityRegistry.getEntityIdentifiers()), false)
                                        .color(Color.RUBY);
                            }
                            return 1;
                        })))
                .then(literal("chat")
                        .then(literal("on").executes(c -> {
                            CONFIG.server.spectator.spectatorPublicChatEnabled = true;
                            c.getSource().getEmbedBuilder()
                                    .title("Spectator Chat Enabled")
                                    .color(Color.CYAN);
                        }))
                        .then(literal("off").executes(c -> {
                            CONFIG.server.spectator.spectatorPublicChatEnabled = false;
                            c.getSource().getEmbedBuilder()
                                    .title("Spectator Chat Disabled")
                                    .color(Color.CYAN);
                        })));
    }

    private String whitelistToString() {
        return CONFIG.server.spectator.whitelist.isEmpty()
                ? "Empty"
                : String.join("\n",
                CONFIG.server.spectator.whitelist.stream()
                        .map(mp -> escape(mp.username + " [" + mp.uuid.toString() + "]"))
                        .collect(Collectors.toList()));
    }
}
