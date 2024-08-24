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
    ScoreboardManager manager;
    Scoreboard board;
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
        zombieTeam.setAllowFriendlyFire(false);
    }

    public void removeZombiePlayer(UUID player) {
        zombieTeam.removePlayer(plugin.getServer().getOfflinePlayer(player));
    }

    public void addZombiePlayer(UUID player) {
        zombieTeam.addPlayer(plugin.getServer().getOfflinePlayer(player));
    }

}
