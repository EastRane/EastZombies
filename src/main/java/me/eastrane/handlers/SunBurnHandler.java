package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.ConfigProvider;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SunBurnHandler extends BaseHandler {
    private ConfigProvider configProvider;
    private BukkitTask task;
    boolean sunBurnHelmetProtection;
    int sunBurnDamage, sunBurnHelmetDurabilityLoss;

    public SunBurnHandler(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
    }

    protected boolean shouldRegister(long[] worldTime) {
        configProvider = plugin.getConfigProvider();
        return configProvider.isSunBurn() &&
                (worldTime[0] > configProvider.getSunBurnDay()) ||
                (worldTime[0] == configProvider.getSunBurnDay() && !configProvider.isSunBurnAtNight()) ||
                (worldTime[0] == configProvider.getSunBurnDay() && configProvider.isSunBurnAtNight() && worldTime[1] >= 13000);
    }

    @Override
    protected boolean register() {
        if (task == null || task.isCancelled()) {
            super.register();
            sunBurnDamage = plugin.getConfigProvider().getSunBurnDamage();
            sunBurnHelmetProtection = configProvider.isSunBurnHelmetProtection();
            sunBurnHelmetDurabilityLoss = configProvider.getSunBurnHelmetDurabilityLoss();
            activateTask();
            return true;
        }
        return false;
    }

    @Override
    protected boolean unregister() {
        if (isReloadable && task != null && !task.isCancelled()) {
            super.unregister();
            deactivateTask();
            return true;
        }
        return false;
    }

    private void activateTask() {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (plugin.getBaseStorage().isZombie(player)) {
                        checkSunBurn(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 40L);
    }

    private void deactivateTask() {
        task.cancel();
    }

    private void checkSunBurn(Player player) {
        World world = player.getWorld();
        if (world.isDayTime() && world.getHighestBlockAt(player.getLocation()).getY() <= player.getLocation().getY() && !player.isInWaterOrRain()) {
            ItemStack helmet = player.getInventory().getHelmet();
            if (helmet != null && sunBurnHelmetProtection) {
                Damageable itemMeta = (Damageable) helmet.getItemMeta();
                itemMeta.setDamage(itemMeta.getDamage() + sunBurnHelmetDurabilityLoss);
                helmet.setItemMeta(itemMeta);
                if (helmet.getType().getMaxDurability() <= itemMeta.getDamage()) {
                    player.getInventory().setHelmet(null);
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1f, 1f);
                    player.setVisualFire(false);
                }
                return;
            }
            damageZombie(player);
        } else {
            player.setVisualFire(false);
        }
    }

    private void damageZombie(Player player) {
        player.setVisualFire(true);
        if (sunBurnDamage > 0) {
            player.damage(sunBurnDamage);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.damage(sunBurnDamage), 20L);
        }
    }
}