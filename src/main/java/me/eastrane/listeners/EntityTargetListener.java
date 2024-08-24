package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTargetListener extends BaseListener implements Listener {
    private ConfigProvider configProvider;

    public EntityTargetListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        configProvider = plugin.getConfigProvider();
        return configProvider.isTarget() &&
                (worldTime[0] > configProvider.getTargetDay()) ||
                (worldTime[0] == configProvider.getTargetDay() && !configProvider.isTargetAtNight()) ||
                (worldTime[0] == configProvider.getTargetDay() && configProvider.isTargetAtNight() && worldTime[1] >= 13000);
    }

    @EventHandler
    public void onHostileMobTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        if (target instanceof Player) {
            Player player = (Player) target;
            if (plugin.getDataManager().isZombiePlayer(player) && entity instanceof Monster) {
                event.setCancelled(true);
            }
        }
    }

}
