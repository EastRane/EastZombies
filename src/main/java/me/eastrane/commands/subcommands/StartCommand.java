package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class StartCommand extends SubCommand {
    private final DataManager dataManager;
    private final LanguageManager languageManager;

    public StartCommand(EastZombies plugin) {
        this.plugin = plugin;
        dataManager = plugin.getDataManager();
        languageManager = plugin.getLanguageManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            languageManager.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        World world = plugin.getServer().getWorlds().get(0);
        world.setFullTime(0);
        for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            if (dataManager.isZombiePlayer(player)) {
                dataManager.removeZombiePlayer(player.getUniqueId());
            }
        }
        plugin.getFeaturesManager().reactivateEvents();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
