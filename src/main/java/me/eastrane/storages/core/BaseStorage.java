package me.eastrane.storages.core;

import me.eastrane.EastZombies;
import me.eastrane.utilities.DebugProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class BaseStorage {
    protected EastZombies plugin;
    protected DebugProvider debugProvider;
    protected final Map<UUID, String> zombies = new LinkedHashMap<>();

    public BaseStorage(EastZombies plugin) {
        this.plugin = plugin;
        debugProvider = plugin.getDebugProvider();
    }

    /**
     * Loads the zombie player data from the data file.
     */
    public abstract void loadStorage();

    /**
     * Adds a player to the zombie player data and saves the changes.
     *
     * @param player The player to add as a zombie.
     */
    public abstract void addZombie(Player player);

    /**
     * Removes a player from the zombie player data and saves the changes.
     *
     * @param player The UUID of the player to remove from the zombie data.
     */
    public abstract void removeZombie(UUID player);

    /**
     * Checks if a player is a zombie.
     *
     * @param player The player to check.
     * @return True if the player is a zombie, false otherwise.
     */
    public boolean isZombie(Player player) {
        return zombies.containsKey(player.getUniqueId());
    }

    /**
     * Checks if an offline player is a zombie.
     *
     * @param offlinePlayer The offline player to check.
     * @return True if the offline player is a zombie, false otherwise.
     */
    public boolean isZombie(OfflinePlayer offlinePlayer) {
        return zombies.containsKey(offlinePlayer.getUniqueId());
    }
}
