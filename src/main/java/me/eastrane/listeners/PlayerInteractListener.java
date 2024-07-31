package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.items.core.CooldownManager;
import me.eastrane.items.core.CustomItemType;
import me.eastrane.items.core.ItemManager;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerInteractListener extends BaseListener implements Listener {
    private final ConfigManager configManager;
    private final LanguageManager languageManager;
    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final String zombieCompassIdentifier;

    public PlayerInteractListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        this.configManager = plugin.getConfigManager();
        this.languageManager = plugin.getLanguageManager();
        this.itemManager = plugin.getItemManager();
        this.cooldownManager = plugin.getCooldownManager();
        zombieCompassIdentifier = itemManager.getCustomItem(CustomItemType.ZOMBIE_COMPASS).getIdentifier();
        cooldownManager.registerCooldown(zombieCompassIdentifier, configManager.getZombieCompassCooldown());
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    @EventHandler
    public void onCompassInteract(PlayerInteractEvent event) {
        if (event.getAction().isRightClick() && event.getItem() != null) {
            ItemStack item = event.getItem();
            if (isZombieCompass(item) && plugin.getItemManager().getCustomItem(CustomItemType.ZOMBIE_COMPASS).isRegistered()) {
                Player player = event.getPlayer();
                if (!cooldownManager.isInCooldown(zombieCompassIdentifier, player)) {
                    handleCompassUse(player, item);
                }
            }
        }
    }

    private boolean isZombieCompass(ItemStack item) {
        if (item.getType() == Material.COMPASS) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String persistentString = meta.getPersistentDataContainer().get(itemManager.getCustomItem(CustomItemType.ZOMBIE_COMPASS).getKey(), PersistentDataType.STRING);
                return zombieCompassIdentifier.equals(persistentString);
            }
        }
        return false;
    }

    private void handleCompassUse(Player player, ItemStack item) {
        Player nearestPlayer = getNearestPlayer(player);
        cooldownManager.setCooldown(zombieCompassIdentifier, player);
        if (nearestPlayer != null) {
            updateCompassTarget(player, item, nearestPlayer);
        } else {
            languageManager.sendMessage(player, "buffs.zombie_compass.no_result");
        }
    }

    private void updateCompassTarget(Player player, ItemStack item, Player target) {
        Location targetLocation = target.getLocation();
        CompassMeta compassMeta = (CompassMeta) item.getItemMeta();
        compassMeta.setLodestone(targetLocation);
        compassMeta.setLodestoneTracked(false);
        item.setItemMeta(compassMeta);
        languageManager.sendMessage(player, "buffs.zombie_compass.tracked", target.getName(), (int) target.getLocation().distance(player.getLocation()));
    }

    private Player getNearestPlayer(Player player) {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        Location playerLocation = player.getLocation();

        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            if (!target.equals(player) && !plugin.getDataManager().isZombiePlayer(target)) {
                double distance = target.getLocation().distance(playerLocation);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = target;
                }
            }
        }
        return nearestPlayer;
    }
}
