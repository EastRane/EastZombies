package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.ConfigProvider;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BorderShrinkHandler extends BaseHandler {
    private ConfigProvider configProvider;
    private BukkitTask task;
    private WorldBorder worldBorder;

    public BorderShrinkHandler(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
    }

    protected boolean shouldRegister(long[] worldTime) {
        this.configProvider = plugin.getConfigProvider();
        return configProvider.isWorldBorderShrinkEnabled() &&
                (configProvider.getWorldBorderInitialRadius() > 0) &&
                (worldTime[0] >= configProvider.getWorldBorderShrinkStartDay());
    }

    @Override
    protected boolean register() {
        if (task == null || task.isCancelled()) {
            super.register();
            worldBorder = plugin.getServer().getWorlds().get(0).getWorldBorder();
            activateTask();
            return true;
        }
        return false;
    }

    @Override
    protected boolean unregister() {
        if (isReloadable && task != null && !task.isCancelled()) {
            super.unregister();
            task.cancel();
            worldBorder.setSize(configProvider.getWorldBorderInitialRadius() * 2);
            return true;
        }
        return false;
    }

    private void activateTask() {
        double initialRadius = configProvider.getWorldBorderInitialRadius();
        int shrinkInterval = configProvider.getWorldBorderShrinkInterval();
        int shrinkAmount = configProvider.getWorldBorderShrinkAmount();
        int shrinkDuration = configProvider.getWorldBorderShrinkDuration();
        double minRadius = configProvider.getWorldBorderShrinkMinRadius();

        task = new BukkitRunnable() {
            @Override
            public void run() {
                double currentSize = worldBorder.getSize();
                if (initialRadius > 0 && currentSize > initialRadius) {
                    double centerX = configProvider.getWorldBorderCenterX();
                    double centerZ = configProvider.getWorldBorderCenterZ();
                    worldBorder.setCenter(centerX, centerZ);
                }
                double newSize = Math.max(currentSize - (shrinkAmount * 2), minRadius * 2);
                if (newSize > minRadius * 2) {
                    worldBorder.setSize(newSize, shrinkDuration);
                    plugin.getLanguageProvider().broadcastMessage("broadcasts.border_shrink", shrinkAmount, shrinkDuration);
                }
            }
        }.runTaskTimer(plugin, 0L, shrinkInterval * 20L); // Convert minutes to ticks
    }
}