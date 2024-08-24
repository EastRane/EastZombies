package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.ConfigProvider;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SunBurnHandler extends BaseHandler {
    private ConfigProvider configProvider;
    private BukkitTask task;

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
            player.setVisualFire(true);
            int sunburnDamage = plugin.getConfigProvider().getSunBurnDamage();
            if (sunburnDamage > 0) {
                player.damage(sunburnDamage);
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.damage(sunburnDamage), 20L);
            }
        } else {
            player.setVisualFire(false);
        }
    }
}