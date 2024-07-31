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

    public void registerCooldown(String itemKey, long duration) {
        cooldownDurations.put(itemKey, duration / 20 * 1000L);
        cooldowns.put(itemKey, new HashMap<>());
    }

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

    public void setCooldown(String itemId, Player player) {
        cooldowns.get(itemId).put(player.getUniqueId(), System.currentTimeMillis());
    }
}