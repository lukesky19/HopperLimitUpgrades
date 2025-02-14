package com.github.lukesky19.hopperlimitupgrades.command;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.gui.UpgradeGUI;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIManager;
import com.github.lukesky19.hopperlimitupgrades.manager.LimitManager;
import com.github.lukesky19.skylib.format.FormatUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Objects;

public class UpgradeCommand {
    public static LiteralCommandNode<CommandSourceStack> createCommand(HopperLimitUpgrades plugin, GUIManager guiManager) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("hopperlimitupgrades").executes(ctx -> {
            final CommandSender sender = ctx.getSource().getSender();

            if (sender instanceof Player player) {
                if(sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades") && sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.gui")) {
                    Island island = BentoBox.getInstance().getIslands().getIsland(Objects.requireNonNull(Bukkit.getWorld("bskyblock_world")), User.getInstance(player.getUniqueId()));
                    if(island == null) {
                        player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You must have an island to upgrade your hopper limit.</red>"));

                        return 0;
                    } else {
                        UpgradeGUI gui = new UpgradeGUI(plugin, guiManager, player);

                        gui.openInventory(plugin, player);

                        return Command.SINGLE_SUCCESS;
                    }
                } else {
                    ctx.getSource().getSender().sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have permission for this command.</red>"));
                    return 0;
                }
            } else {
                sender.sendMessage(FormatUtil.format("<red>This command can only be ran by a player.<red>"));

                return 0;
            }
        });

        builder.then(Commands.literal("reset")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();

                            if(sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades") && sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.reset")) {
                                return LimitManager.setHopperLimitOffset(ctx.getSource().getSender(), player, 0) ? 1 : 0;
                            } else {
                                ctx.getSource().getSender().sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have permission for this command.</red>"));
                                return 0;
                            }
                        })));

        builder.then(Commands.literal("set")
                .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("amount", ArgumentTypes.integerRange())
                                        .executes(ctx -> {
                                            CommandSender sender = ctx.getSource().getSender();
                                            Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                                            int amount = ctx.getArgument("amount", IntegerRangeProvider.class).range().lowerEndpoint();

                                            if(sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades") && sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.set")) {
                                                return LimitManager.setHopperLimitOffset(ctx.getSource().getSender(), player, amount) ? 1 : 0;
                                            } else {
                                                ctx.getSource().getSender().sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have permission for this command.</red>"));
                                                return 0;
                                            }
                                        }))));

        builder.then(Commands.literal("add")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.argument("amount", ArgumentTypes.integerRange())
                                .executes(ctx -> {
                                    CommandSender sender = ctx.getSource().getSender();
                                    Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                                    int amount = ctx.getArgument("amount", IntegerRangeProvider.class).range().lowerEndpoint();

                                    if(sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades") && sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.add")) {
                                        return LimitManager.addHopperLimitOffset(ctx.getSource().getSender(), player, amount) ? 1 : 0;
                                    } else {
                                        ctx.getSource().getSender().sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have permission for this command.</red>"));
                                        return 0;
                                    }
                                }))));

        builder.then(Commands.literal("remove")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.argument("amount", ArgumentTypes.integerRange())
                                .executes(ctx -> {
                                    CommandSender sender = ctx.getSource().getSender();
                                    Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                                    int amount = ctx.getArgument("amount", IntegerRangeProvider.class).range().lowerEndpoint();

                                    if(sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades") && sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.remove")) {
                                        return LimitManager.removeHopperLimitOffset(ctx.getSource().getSender(), player, amount) ? 1 : 0;
                                    } else {
                                        ctx.getSource().getSender().sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have permission for this command.</red>"));
                                        return 0;
                                    }
                                }))));

        builder.then(Commands.literal("get")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(ctx -> {
                            CommandSender sender = ctx.getSource().getSender();
                            Player player = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();

                            if(sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades") && sender.hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.get")) {
                                return LimitManager.getHopperLimitOffset(ctx.getSource().getSender(), player) ? 1 : 0;
                            } else {
                                ctx.getSource().getSender().sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have permission for this command.</red>"));
                                return 0;
                            }
                        })));

        return builder.build();
    }
}
