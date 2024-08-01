package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RemoveCommand extends SubCommand {
    private final DataManager dataManager;
    private final LanguageManager languageManager;

    public RemoveCommand(EastZombies plugin) {
        this.plugin = plugin;
        dataManager = plugin.getDataManager();
        languageManager = plugin.getLanguageManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length < 2) {
            languageManager.sendMessage(sender, "commands.errors.too_few_arguments");
            return;
        } else if (args.length > 2) {
            languageManager.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        UUID playerId = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
        if (!dataManager.isZombiePlayer(plugin.getServer().getOfflinePlayer(playerId))) {
            languageManager.sendMessage(sender, "commands.remove.zombie_not_found");
            return;
        }
        dataManager.removeZombiePlayer(playerId);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();
        if (args.length == 2) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                list.add(p.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
