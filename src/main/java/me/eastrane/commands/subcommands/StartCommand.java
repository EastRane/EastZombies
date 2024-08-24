package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StartCommand extends SubCommand {
    private final BaseStorage baseStorage;
    private final LanguageProvider languageProvider;

    public StartCommand(EastZombies plugin) {
        this.plugin = plugin;
        baseStorage = plugin.getBaseStorage();
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
            if (baseStorage.isZombie(player)) {
                UUID playerId = player.getUniqueId();
                plugin.getPlayerManager().removeZombie(playerId);
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
