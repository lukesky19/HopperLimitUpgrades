package com.github.lukesky19.hopperlimitupgrades.command;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.config.Locale;
import com.github.lukesky19.hopperlimitupgrades.gui.UpgradeGUI;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIConfigManager;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIManager;
import com.github.lukesky19.hopperlimitupgrades.manager.LimitManager;
import com.github.lukesky19.hopperlimitupgrades.manager.LocaleManager;
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Optional;
import java.util.UUID;

/**
 * This class is used to create the /hopperlimitupgrades command.
 */
public class UpgradeCommand {
    private final @NotNull HopperLimitUpgrades hopperLimitUpgrades;
    private final @NotNull ComponentLogger logger;
    private final @NotNull LocaleManager localeManager;
    private final @NotNull GUIConfigManager guiConfigManager;
    private final @NotNull GUIManager guiManager;
    private final @NotNull LimitManager limitManager;

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     * @param localeManager A {@link LocaleManager} instance.
     * @param guiConfigManager A {@link GUIConfigManager} instance.
     * @param guiManager A {@link GUIManager} instance.
     * @param limitManager A {@link LimitManager} instance.
     */
    public UpgradeCommand(
            @NotNull HopperLimitUpgrades hopperLimitUpgrades,
            @NotNull LocaleManager localeManager,
            @NotNull GUIConfigManager guiConfigManager,
            @NotNull GUIManager guiManager,
            @NotNull LimitManager limitManager) {
        this.hopperLimitUpgrades = hopperLimitUpgrades;
        this.logger = hopperLimitUpgrades.getComponentLogger();
        this.localeManager = localeManager;
        this.guiConfigManager = guiConfigManager;
        this.guiManager = guiManager;
        this.limitManager = limitManager;
    }

    /**
     * Create the {@link LiteralCommandNode} of type {@link CommandSourceStack} for the /hopperlimitupgrades command.
     * @return A {@link LiteralCommandNode} of type {@link CommandSourceStack} for the /hopperlimitupgrades command.
     */
    public @NotNull LiteralCommandNode<CommandSourceStack> createCommand() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("hopperlimitupgrades")
            .requires(ctx -> ctx.getSender().hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades"));

        builder.executes(ctx -> {
            @NotNull Locale locale = localeManager.getLocale();
            if(!(ctx.getSource().getSender() instanceof Player player)) {
                logger.info(AdventureUtil.serialize(locale.playerOnly()));
                return 0;
            }
            UUID uuid = player.getUniqueId();

            Optional<Island> optionalIsland = BentoBox.getInstance().getIslandsManager().getIslandAt(player.getLocation());
            if(optionalIsland.isEmpty()) {
                player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.notOnIsland()));
                return 0;
            }
            Island island = optionalIsland.get();

            if(!island.getMemberSet().contains(uuid)) {
                player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.islandMemberOrOwnerOnly()));
                return 0;
            }

            UpgradeGUI gui = new UpgradeGUI(hopperLimitUpgrades, guiConfigManager, guiManager, localeManager, player, island);
            boolean createResult = gui.create();
            if(!createResult) {
                player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.guiOpenError()));
                return 0;
            }

            boolean updateResult = gui.update();
            if(!updateResult) {
                player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.guiOpenError()));
                return 0;
            }

            boolean openResult = gui.open();
            if(!openResult) {
                player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.guiOpenError()));
                return 0;
            }

            return 1;
        });

        builder.then(Commands.literal("reset")
            .requires(ctx -> ctx.getSender().hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.reset"))
            .then(Commands.argument("player", ArgumentTypes.player())
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    Player targetPlayer = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();

                    return limitManager.setHopperLimitOffset(sender, targetPlayer, 0) ? 1 : 0;
                })));
        
        builder.then(Commands.literal("reload")
            .requires(ctx -> ctx.getSender().hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.reload"))
            .executes(ctx -> {
                Locale locale = localeManager.getLocale();
                CommandSender sender = ctx.getSource().getSender();

                if(sender instanceof Player player) {
                    player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.reload()));
                } else {
                    logger.info(AdventureUtil.serialize(locale.reload()));
                }

                return 1;
            }));

        builder.then(Commands.literal("set")
            .requires(ctx -> ctx.getSender().hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.set"))
            .then(Commands.argument("player", ArgumentTypes.player())
                .then(Commands.argument("amount", ArgumentTypes.integerRange())
                    .executes(ctx -> {
                        CommandSender sender = ctx.getSource().getSender();
                        Player targetPlayer = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                        int amount = ctx.getArgument("amount", IntegerRangeProvider.class).range().lowerEndpoint();

                        return limitManager.setHopperLimitOffset(sender, targetPlayer, amount) ? 1 : 0;
                    }))));

        builder.then(Commands.literal("add")
            .requires(ctx -> ctx.getSender().hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.add"))
            .then(Commands.argument("player", ArgumentTypes.player())
                .then(Commands.argument("amount", ArgumentTypes.integerRange())
                    .executes(ctx -> {
                        CommandSender sender = ctx.getSource().getSender();
                        Player targetPlayer = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                        int amount = ctx.getArgument("amount", IntegerRangeProvider.class).range().lowerEndpoint();

                        return limitManager.addHopperLimitOffset(sender, targetPlayer, amount) ? 1 : 0;
                    }))));

        builder.then(Commands.literal("remove")
            .requires(ctx -> ctx.getSender().hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.remove"))
            .then(Commands.argument("player", ArgumentTypes.player())
                .then(Commands.argument("amount", ArgumentTypes.integerRange())
                    .executes(ctx -> {
                        CommandSender sender = ctx.getSource().getSender();
                        Player targetPlayer = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                        int amount = ctx.getArgument("amount", IntegerRangeProvider.class).range().lowerEndpoint();

                        return limitManager.removeHopperLimitOffset(sender, targetPlayer, amount) ? 1 : 0;
                    }))));

        builder.then(Commands.literal("get")
            .requires(ctx -> ctx.getSender().hasPermission("hopperlimitupgrades.commands.hopperlimitupgrades.get"))
            .then(Commands.argument("player", ArgumentTypes.player())
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    Player targetPlayer = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();

                    return limitManager.sendHopperLimitOffsetMessage(sender, targetPlayer) ? 1 : 0;
                })));

        return builder.build();
    }
}
