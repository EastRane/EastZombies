package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(EastZombies plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length != 1) {
            plugin.getLanguageProvider().sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        plugin.getConfigProvider().reloadConfig();
        plugin.getDataManager().loadData();
        plugin.getLanguageProvider().sendMessage(sender, "commands.reload.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
