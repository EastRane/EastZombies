package me.eastrane.storages;

import me.eastrane.EastZombies;
import me.eastrane.storages.core.BaseStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YamlStorage extends BaseStorage {
    private final File dataFile;
    private FileConfiguration dataConfig;

    public YamlStorage(EastZombies plugin) {
        super(plugin);
        dataFile = new File(plugin.getDataFolder(), "zombies.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                debugProvider.sendException(e);
            }
        }
        loadStorage();
    }

    @Override
    public void loadStorage() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        zombies.clear();
        if (dataConfig.contains("players")) {
            List<String> playerNames = dataConfig.getStringList("players");
            for (String uuidString : playerNames) {
                UUID player = UUID.fromString(uuidString);
                // In the future there will be ZombieData with additional info instead of null
                zombies.put(player, null);
            }
            if (!zombies.isEmpty()) {
                debugProvider.sendInfo(zombies.size() + " zombies were loaded from YAML storage.", true);
            }
        }
    }

    /**
     * Saves the zombie player data to the data file.
     */
    public void saveStorage() {
        List<String> uuidStrings = new ArrayList<>();
        for (UUID uuid : zombies.keySet()) {
            uuidStrings.add(uuid.toString());
        }
        dataConfig.set("players", uuidStrings);
        try {
            dataConfig.save(dataFile);
            debugProvider.sendInfo("YAML storage was saved successfully.");
        } catch (IOException e) {
            debugProvider.sendException(e);
        }
    }

    /**
     * Adds a player to the zombie player data and saves the changes.
     *
     * @param player The player to add as a zombie.
     */
    public void addZombie(Player player) {
        debugProvider.sendInfo(player.getName() + " was added to YAML storage as a zombie.");
        zombies.put(player.getUniqueId(), null);
        saveStorage();
    }

    /**
     * Removes a player from the zombie player data and saves the changes.
     *
     * @param player The UUID of the player to remove from the zombie data.
     */
    public void removeZombie(UUID player) {
        debugProvider.sendInfo(plugin.getServer().getOfflinePlayer(player).getName() + " was removed from YAML storage.");
        zombies.remove(player);
        saveStorage();
    }
}