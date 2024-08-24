package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class StartCommand extends SubCommand {
    private final DataManager dataManager;
    private final LanguageProvider languageProvider;

    public StartCommand(EastZombies plugin) {
        this.plugin = plugin;
        dataManager = plugin.getDataManager();
        languageProvider = plugin.getLanguageProvider();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length != 1) {
            languageProvider.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        World world = plugin.getServer().getWorlds().get(0);
        world.setFullTime(0);
        world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 1000);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            if (dataManager.isZombiePlayer(player)) {
                dataManager.removeZombiePlayer(player.getUniqueId());
            }
        }
        if (plugin.getConfigProvider().isVoicePersistentGroups() && plugin.getVoiceHandler() != null) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.getVoiceHandler().connectToTeamGroup(plugin.getServer().getPlayer(player.getUniqueId()));
            }
        }
        plugin.getFeaturesManager().reactivateEvents();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
