package com.zenith.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.zenith.command.Command;
import com.zenith.command.CommandUsage;
import com.zenith.command.brigadier.CommandCategory;
import com.zenith.command.brigadier.CommandContext;
import com.zenith.discord.Embed;
import com.zenith.module.impl.AutoEat;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.zenith.Shared.CONFIG;
import static com.zenith.Shared.MODULE;
import static com.zenith.command.brigadier.ToggleArgumentType.getToggle;
import static com.zenith.command.brigadier.ToggleArgumentType.toggle;
import static java.util.Arrays.asList;

public class AutoEatCommand extends Command {
    @Override
    public CommandUsage commandUsage() {
        return CommandUsage.args("autoEat",
                                 CommandCategory.MODULE,
                                 "Configures the AutoEat feature",
                                 asList("on/off", "health <int>", "hunger <int>", "warning on/off"));
    }

    @Override
    public LiteralArgumentBuilder<CommandContext> register() {
        return command("autoEat")
            .then(argument("toggle", toggle()).executes(c -> {
                CONFIG.client.extra.autoEat.enabled = getToggle(c, "toggle");
                MODULE.get(AutoEat.class).syncEnabledFromConfig();
                c.getSource().getEmbed()
                    .title("AutoEat " + toggleStrCaps(CONFIG.client.extra.autoEat.enabled));
                return OK;
            }))
            .then(literal("health")
                      .then(argument("health", integer(1, 19)).executes(c -> {
                          int health = IntegerArgumentType.getInteger(c, "health");
                          CONFIG.client.extra.autoEat.healthThreshold = health;
                          c.getSource().getEmbed()
                              .title("AutoEat Health Threshold Set")
                              .primaryColor()
                              .addField("Health Threshold", CONFIG.client.extra.autoEat.healthThreshold, false)
                              .addField("Hunger Threshold", CONFIG.client.extra.autoEat.hungerThreshold, false)
                              .addField("Warning", Boolean.toString(CONFIG.client.extra.autoEat.warning), false);
                          return OK;
                      })))
            .then(literal("hunger")
                      .then(argument("hunger", integer(1, 19)).executes(c -> {
                          int hunger = IntegerArgumentType.getInteger(c, "hunger");
                          CONFIG.client.extra.autoEat.hungerThreshold = hunger;
                          c.getSource().getEmbed()
                              .title("AutoEat Hunger Threshold Set")
                              .primaryColor()
                              .addField("Health Threshold", CONFIG.client.extra.autoEat.healthThreshold, false)
                              .addField("Hunger Threshold", CONFIG.client.extra.autoEat.hungerThreshold, false)
                              .addField("Warning", Boolean.toString(CONFIG.client.extra.autoEat.warning), false);
                          return OK;
                      })))
            .then(literal("warning")
                      .then(argument("toggle", toggle()).executes(c -> {
                            CONFIG.client.extra.autoEat.warning = getToggle(c, "toggle");
                            c.getSource().getEmbed()
                                .title("AutoEat Warning " + toggleStrCaps(CONFIG.client.extra.autoEat.warning));
                            return OK;
                      })));
    }

    @Override
    public void postPopulate(final Embed builder) {
        builder
            .addField("AutoEat", toggleStr(CONFIG.client.extra.autoEat.enabled), false)
            .addField("Health Threshold", CONFIG.client.extra.autoEat.healthThreshold, false)
            .addField("Hunger Threshold", CONFIG.client.extra.autoEat.hungerThreshold, false)
            .addField("Warning", Boolean.toString(CONFIG.client.extra.autoEat.warning), false)
            .primaryColor();
    }
}
