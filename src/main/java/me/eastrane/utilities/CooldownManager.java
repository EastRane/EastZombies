package me.eastrane.utilities;

import me.eastrane.EastZombies;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private final LanguageProvider languageProvider;
    private final Map<String, Map<UUID, Long>> cooldowns;
    private final Map<String, Long> cooldownDurations;

    public CooldownManager(EastZombies plugin) {
        this.cooldowns = new HashMap<>();
        this.cooldownDurations = new HashMap<>();
        languageProvider = plugin.getLanguageProvider();
    }

    /**
     * Registers a new cooldown.
     *
     * @param id The unique identifier of an action.
     * @param duration The duration of the cooldown in ticks (20 ticks = 1 second).
     */
    public void registerCooldown(String id, long duration) {
        cooldownDurations.put(id, duration / 20 * 1000L);
        cooldowns.put(id, new HashMap<>());
    }

    /**
     * Checks if a player is in cooldown for a specific action.
     *
     * @param id The unique identifier of an action.
     * @param player The player to check.
     * @return True if the player is in cooldown, false otherwise.
     */
    public boolean isInCooldown(String id, Player player) {
        UUID playerId = player.getUniqueId();
        Map<UUID, Long> actionCooldowns = cooldowns.get(id);
        if (actionCooldowns != null && actionCooldowns.containsKey(playerId)) {
            long timeElapsed = System.currentTimeMillis() - actionCooldowns.get(playerId);
            long cooldownDuration = cooldownDurations.get(id);
            if (timeElapsed < cooldownDuration) {
                long timeLeft = ((cooldownDuration - timeElapsed) / 1000) + 1;
                languageProvider.sendMessage(player, "buffs." + id + ".cooldown", timeLeft);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Sets a cooldown for a specific action for a player.
     *
     * @param id The unique identifier of an action.
     * @param player The player to set the cooldown for.
     */
    public void setCooldown(String id, Player player) {
        cooldowns.get(id).put(player.getUniqueId(), System.currentTimeMillis());
    }
}