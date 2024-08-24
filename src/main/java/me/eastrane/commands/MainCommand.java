package me.eastrane.commands;

import me.eastrane.EastZombies;
import me.eastrane.commands.subcommands.*;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final LanguageProvider languageProvider;
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    public MainCommand(EastZombies plugin) {
        languageProvider = plugin.getLanguageProvider();
        registerSubCommand("remove", new RemoveCommand(plugin));
        registerSubCommand("start", new StartCommand(plugin));
        registerSubCommand("reload", new ReloadCommand(plugin));
        registerSubCommand("status", new StatusCommand(plugin));
    }

    private void registerSubCommand(String command, SubCommand subCommand) {
        subCommands.put(command, subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("eastzombies.help")) {
                languageProvider.sendMessage(sender, "commands.help");
                return true;
            }
            languageProvider.sendMessage(sender, "commands.errors.no_permission");
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null) {
            subCommand.execute(sender, args);
        } else {
            String availableCommands = String.join(", ", subCommands.keySet());
            languageProvider.sendMessage(sender, "commands.errors.invalid_subcommand", availableCommands);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream()
                    .filter(subCommand -> subCommand.startsWith(args[0].toLowerCase()))
                    .toList();
        } else {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                return subCommand.onTabComplete(sender, args);
            }
        }
        return null;
    }
}
