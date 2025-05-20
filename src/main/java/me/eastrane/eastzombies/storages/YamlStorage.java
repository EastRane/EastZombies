package me.eastrane.eastzombies.storages;

import me.eastrane.eastzombies.EastZombies;
import me.eastrane.eastzombies.storages.core.BaseStorage;
import me.eastrane.eastzombies.storages.core.ZombieData;
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
            List<Map<?, ?>> playerDataList = dataConfig.getMapList("players");
            for (Map<?, ?> playerData : playerDataList) {
                String uuidString = (String) playerData.get("uuid");
                UUID player = UUID.fromString(uuidString);
                String zombieType = (String) playerData.get("type");
                ZombieData zombieData = new ZombieData(zombieType);
                zombies.put(player, zombieData);
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
        List<Map<String, Object>> playerDataList = new ArrayList<>();
        for (UUID uuid : zombies.keySet()) {
            Map<String, Object> playerData = new LinkedHashMap<>();
            playerData.put("uuid", uuid.toString());
            ZombieData zombieData = zombies.get(uuid);
            if (zombieData != null) {
                playerData.put("type", zombieData.getZombieType());
            } else {
                playerData.put("type", "none");
            }
            playerDataList.add(playerData);
        }
        dataConfig.set("players", playerDataList);
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
    public void addZombie(Player player, String zombieType) {
        debugProvider.sendInfo(player.getName() + " was added to YAML storage as a zombie.");
        ZombieData zombieData = new ZombieData(zombieType);
        zombies.put(player.getUniqueId(), zombieData);
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