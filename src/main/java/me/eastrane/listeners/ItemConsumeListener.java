package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigProvider;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemConsumeListener extends BaseListener implements Listener {
    private ConfigProvider configProvider;

    public ItemConsumeListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        configProvider = plugin.getConfigProvider();
        return configProvider.isFlesh() &&
                (worldTime[0] > configProvider.getFleshDay()) ||
                (worldTime[0] == configProvider.getFleshDay() && !configProvider.isFleshAtNight()) ||
                (worldTime[0] == configProvider.getFleshDay() && configProvider.isFleshAtNight() && worldTime[1] >= 13000);
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.ROTTEN_FLESH) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().removePotionEffect(PotionEffectType.HUNGER);
                }
            }.runTaskLater(plugin, 0);
        }
    }
}