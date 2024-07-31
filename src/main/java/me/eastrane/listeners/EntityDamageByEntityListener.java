package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DataManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDamageByEntityListener extends BaseListener implements Listener {
    private ConfigManager configManager;
    private final DataManager dataManager;

    public EntityDamageByEntityListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        dataManager = plugin.getDataManager();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        configManager = plugin.getConfigManager();
        return configManager.isHunger() &&
                (worldTime[0] > configManager.getHungerDay()) ||
                (worldTime[0] == configManager.getHungerDay() && !configManager.isHungerAtNight()) ||
                (worldTime[0] == configManager.getHungerDay() && configManager.isHungerAtNight() && worldTime[1] >= 13000);
    }

    @EventHandler
    private void onPlayerDealDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (damager instanceof Player && entity instanceof Player) {
            if (dataManager.isZombiePlayer(((Player) damager).getPlayer())) {
                if (!dataManager.isZombiePlayer(((Player) entity).getPlayer())) {
                    Player player = (Player) event.getEntity();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, configManager.getHungerDuration(), 0));
                }
            }
        }
    }
}
