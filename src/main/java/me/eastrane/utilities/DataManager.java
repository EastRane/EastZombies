package me.eastrane.utilities;

import me.eastrane.EastZombies;
import me.eastrane.handlers.SkinsHandler;
import me.eastrane.handlers.TeamHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataManager {
    private final EastZombies plugin;
    private final DebugManager debugManager;
    private final TeamHandler teamHandler;
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final Set<UUID> zombiePlayers = new HashSet<>();

    public DataManager(EastZombies plugin) {
        this.plugin = plugin;
        debugManager = plugin.getDebugManager();
        this.teamHandler = plugin.getTeamHandler();
        dataFile = new File(plugin.getDataFolder(), "zombies.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getDebugManager().sendException(e);
            }
        }
        loadData();
    }

    public void loadData() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        if (dataConfig.contains("players")) {
            List<String> stringList = dataConfig.getStringList("players");
            for (String uuidString : stringList) {
                UUID player = UUID.fromString(uuidString);
                zombiePlayers.add(player);
                teamHandler.addZombiePlayer(player);
            }
            if (!zombiePlayers.isEmpty()) {
                debugManager.sendInfo(zombiePlayers.stream().count() + " zombies was loaded from data file.", true);
            }
        }
    }


    public void saveData() {
        List<String> uuidStrings = new ArrayList<>();
        for (UUID uuid : zombiePlayers) {
            uuidStrings.add(uuid.toString());
        }
        dataConfig.set("players", uuidStrings);
        try {
            dataConfig.save(dataFile);
            debugManager.sendInfo("Data file was saved successfully.");
        } catch (IOException e) {
            debugManager.sendException(e);
        }
    }

    public void addZombiePlayer(Player player) {
        debugManager.sendInfo(player.getName() + " was added to data file as a zombie.");
        zombiePlayers.add(player.getUniqueId());
        teamHandler.addZombiePlayer(player.getUniqueId());
        saveData();
    }

    public void removeZombiePlayer(UUID player) {
        debugManager.sendInfo(plugin.getServer().getOfflinePlayer(player).getName() + " was removed from data file.");
        zombiePlayers.remove(player);
        saveData();
        debugManager.sendInfo(plugin.getServer().getOfflinePlayer(player).getName() + " was removed from vanilla team.");
        teamHandler.removeZombiePlayer(player);
        plugin.getEffectsHandler().clearEffects(plugin.getServer().getPlayer(player));
        try {
            plugin.getSkinsHandler().clearSkin(player);
        } catch (Exception e) {

        }
        // OfflinePlayer is null, so SkinsRestorer throws an exception but still clears the skin.
        // Also, unfortunately there is no way to clear effects from OfflinePlayer, so it was implemented using Join listener
        // And if there is no SkinsRestorer, you cannot clear skin

    }

    public boolean isZombiePlayer(Player player) {
        return zombiePlayers.contains(player.getUniqueId());
    }

    public boolean isZombiePlayer(OfflinePlayer offlinePlayer) {
        return zombiePlayers.contains(offlinePlayer.getUniqueId());
    }
}