package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.storages.core.BaseStorage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HumanDamageByZombieListener extends BaseListener implements Listener {
    private ConfigProvider configProvider;
    private final BaseStorage baseStorage;

    public HumanDamageByZombieListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        baseStorage = plugin.getBaseStorage();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        configProvider = plugin.getConfigProvider();
        return configProvider.isHunger() &&
                (worldTime[0] > configProvider.getHungerDay()) ||
                (worldTime[0] == configProvider.getHungerDay() && !configProvider.isHungerAtNight()) ||
                (worldTime[0] == configProvider.getHungerDay() && configProvider.isHungerAtNight() && worldTime[1] >= 13000);
    }

    @EventHandler
    private void onPlayerDealDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (damager instanceof Player && entity instanceof Player) {
            if (baseStorage.isZombie(((Player) damager).getPlayer())) {
                if (!baseStorage.isZombie(((Player) entity).getPlayer())) {
                    Player player = (Player) event.getEntity();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, configProvider.getHungerDuration(), 0));
                }
            }
        }
    }
}
