package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.LanguageManager;
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
            plugin.getLanguageManager().sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        plugin.getConfigManager().reloadConfig();
        plugin.getDataManager().loadData();
        plugin.getLanguageManager().sendMessage(sender, "commands.reload.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
