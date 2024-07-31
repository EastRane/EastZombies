package me.eastrane.items.core;

import me.eastrane.EastZombies;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private final LanguageManager languageManager;
    private final Map<String, Map<UUID, Long>> cooldowns;
    private final Map<String, Long> cooldownDurations;

    public CooldownManager(EastZombies plugin) {
        this.cooldowns = new HashMap<>();
        this.cooldownDurations = new HashMap<>();
        languageManager = plugin.getLanguageManager();
    }

    /**
     * Registers a new item cooldown.
     *
     * @param itemKey  The unique identifier of the item.
     * @param duration The duration of the cooldown in ticks (20 ticks = 1 second).
     */
    public void registerCooldown(String itemKey, long duration) {
        cooldownDurations.put(itemKey, duration / 20 * 1000L);
        cooldowns.put(itemKey, new HashMap<>());
    }

    /**
     * Checks if a player is in cooldown for a specific item.
     *
     * @param itemId The unique identifier of the item.
     * @param player The player to check.
     * @return True if the player is in cooldown, false otherwise.
     */
    public boolean isInCooldown(String itemId, Player player) {
        UUID playerId = player.getUniqueId();
        Map<UUID, Long> itemCooldowns = cooldowns.get(itemId);
        if (itemCooldowns != null && itemCooldowns.containsKey(playerId)) {
            long timeElapsed = System.currentTimeMillis() - itemCooldowns.get(playerId);
            long cooldownDuration = cooldownDurations.get(itemId);
            if (timeElapsed < cooldownDuration) {
                long timeLeft = ((cooldownDuration - timeElapsed) / 1000) + 1;
                languageManager.sendMessage(player, "buffs." + itemId + ".cooldown", timeLeft);
                return true;
            } else {
                itemCooldowns.remove(playerId);
                return false;
            }
        }
        return false;
    }

    /**
     * Sets a cooldown for a specific item for a player.
     *
     * @param itemId The unique identifier of the item.
     * @param player The player to set the cooldown for.
     */
    public void setCooldown(String itemId, Player player) {
        cooldowns.get(itemId).put(player.getUniqueId(), System.currentTimeMillis());
    }
}