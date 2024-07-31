package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.ConfigManager;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class IronGolemAttackHandler extends BaseHandler {
    private ConfigManager configManager;
    private BukkitTask task;

    public IronGolemAttackHandler(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        configManager = plugin.getConfigManager();
        return configManager.isGolems() &&
                (worldTime[0] > configManager.getGolemsDay()) ||
                (worldTime[0] == configManager.getGolemsDay() && !configManager.isGolemsAtNight()) ||
                (worldTime[0] == configManager.getGolemsDay() && configManager.isGolemsAtNight() && worldTime[1] >= 13000);
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
                    if (plugin.getDataManager().isZombiePlayer(player)) {
                        checkIronGolem(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 40L);
    }

    private void deactivateTask() {
        task.cancel();
    }

    private void checkIronGolem(Player player) {
        player.getWorld().getEntitiesByClass(IronGolem.class).forEach(golem -> {
            if (golem.getLocation().distance(player.getLocation()) < 10) {
                golem.setTarget(player);
            } else if (golem.getTarget() != null && golem.getTarget().equals(player) && golem.getLocation().distance(player.getLocation()) > 20) {
                golem.setTarget(null);
            }
        });
    }
}
