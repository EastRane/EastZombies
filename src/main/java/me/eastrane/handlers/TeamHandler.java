package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class TeamHandler extends BaseHandler {
    private final EastZombies plugin;
    private final ScoreboardManager manager;
    private final Scoreboard board;
    private Team zombieTeam;

    public TeamHandler(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        this.plugin = plugin;
        manager = plugin.getServer().getScoreboardManager();
        board = manager.getMainScoreboard();
        createZombieTeam();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    private void createZombieTeam() {
        zombieTeam = board.getTeam("zombies");
        if (zombieTeam == null) {
            plugin.getDebugProvider().sendInfo("Creating zombies team...");
            board.registerNewTeam("zombies");
            zombieTeam = board.getTeam("zombies");
        }
        zombieTeam.setDisplayName(plugin.getLanguageProvider().getTranslation("main.zombies_team_name"));
        zombieTeam.setColor(ChatColor.DARK_GREEN);
        zombieTeam.setAllowFriendlyFire(plugin.getConfigProvider().isFriendlyFireZombies());
    }

    public void removeZombie(UUID player) {
        zombieTeam.removePlayer(plugin.getServer().getOfflinePlayer(player));
        plugin.getDebugProvider().sendInfo(plugin.getServer().getOfflinePlayer(player).getName() + " was removed from vanilla team.");
    }

    public void addZombie(UUID player) {
        zombieTeam.addPlayer(plugin.getServer().getOfflinePlayer(player));
        plugin.getDebugProvider().sendInfo(plugin.getServer().getOfflinePlayer(player).getName() + " was added to vanilla team.");
    }

}
