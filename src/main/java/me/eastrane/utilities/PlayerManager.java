package me.eastrane.utilities;

import me.eastrane.EastZombies;
import me.eastrane.storages.core.BaseStorage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerManager {
    private EastZombies plugin;
    private final ConfigProvider configProvider;
    private final BaseStorage baseStorage;

    public PlayerManager(EastZombies plugin) {
        this.plugin = plugin;
        configProvider = plugin.getConfigProvider();
        baseStorage = plugin.getBaseStorage();
    }

    public void addZombie(Player player) {
        plugin.getBaseStorage().addZombie(player);
        plugin.getTeamHandler().addZombie(player.getUniqueId());
        if (configProvider.isChangeSkin() && plugin.getSkinsHandler() != null) {
            try {
                plugin.getSkinsHandler().changeSkin(player);
            } catch (Exception ignored) {
                // If SkinsRestorer isn't installed, EastZombies won't see an exception
            }
        }
        plugin.getEffectsHandler().clearEffects(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (configProvider.isEffects() && baseStorage.isZombie(player)) {
                    plugin.getEffectsHandler().giveZombieEffects(player);
                }
                if (configProvider.isVoicePersistentGroups() && configProvider.isVoiceJoinOnDeath() && plugin.getVoiceHandler() != null) {
                    plugin.getVoiceHandler().connectToTeamGroup(player);
                }
            }
        }.runTaskLater(plugin, 0);
    }

    public void removeZombie(UUID player) {
        baseStorage.removeZombie(player);
        plugin.getTeamHandler().removeZombie(player);
        plugin.getEffectsHandler().clearEffects(plugin.getServer().getPlayer(player));
        try {
            plugin.getSkinsHandler().clearSkin(player);
        } catch (Exception ignored) {

        }
        if (plugin.getConfigProvider().isVoicePersistentGroups() && plugin.getVoiceHandler() != null) {
            plugin.getVoiceHandler().connectToTeamGroup(plugin.getServer().getPlayer(player));
        }
    }

}
