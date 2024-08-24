package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.DebugProvider;
import me.eastrane.utilities.LanguageProvider;
import me.eastrane.utilities.DataManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDeathListener extends BaseListener implements Listener {
    private final EastZombies plugin;
    private final DataManager dataManager;
    private final DebugProvider debugProvider;
    private final ConfigProvider configProvider;
    private final LanguageProvider languageProvider;
    private final HashMap<UUID, Location> deathLocations = new HashMap<>();

    public PlayerDeathListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        this.plugin = plugin;
        this.dataManager = plugin.getDataManager();
        this.debugProvider = plugin.getDebugProvider();
        this.configProvider = plugin.getConfigProvider();
        this.languageProvider = plugin.getLanguageProvider();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (configProvider.isDropHead()) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
            headMeta.setOwningPlayer(event.getPlayer());
            playerHead.setItemMeta(headMeta);
            player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), playerHead);
        }
        if (!dataManager.isZombiePlayer(player)) {
            if (configProvider.isResetRespawnOnFirstDeath()) {
                deathLocations.put(player.getUniqueId(), player.getLocation());
            }

            debugProvider.sendInfo(player.getName() + " has just died and become a zombie.");
            plugin.getDataManager().addZombiePlayer(player);
            if (configProvider.isChangeSkin() && plugin.getSkinsHandler() != null) {
                try {
                    plugin.getSkinsHandler().changeSkin(player);
                } catch (Exception ignored) {
                    // If SkinsRestorer isn't installed, EastZombies won't see an exception
                }
            }
            languageProvider.broadcastMessage("broadcasts.player_turned_zombie", player.getName());
        } else {
            debugProvider.sendInfo(player.getName() + " has just died, but he is already a zombie.");
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 1.0f, 1.0f);
            if (configProvider.isDropFlesh()) {
                player.getWorld().dropItemNaturally(event.getPlayer().getLocation(), new ItemStack(Material.ROTTEN_FLESH, configProvider.getDropFleshAmount()));
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (configProvider.isResetRespawnOnFirstDeath() && dataManager.isZombiePlayer(player) && deathLocations.containsKey(playerId)) {
            Location deathLocation = deathLocations.get(playerId);
            // This method doesn't exist before 1.20
            event.setRespawnLocation(deathLocation);
            deathLocations.remove(playerId);
            player.setRespawnLocation(null);
            plugin.getDebugProvider().sendInfo(player.getName() + " was respawned at his death location because he has just turned into a zombie.");
        }

        plugin.getEffectsHandler().clearEffects(player);
        if (configProvider.isEffects() && dataManager.isZombiePlayer(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getEffectsHandler().giveZombieEffects(player);
                }
            }.runTaskLater(plugin, 0);
        }
        if (configProvider.isVoicePersistentGroups() && configProvider.isVoiceJoinOnDeath() && plugin.getVoiceHandler() != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getVoiceHandler().connectToTeamGroup(player);
                }
            }.runTaskLater(plugin, 0);
        }
        player.setNoDamageTicks(configProvider.getInvulnerability());
    }
}
