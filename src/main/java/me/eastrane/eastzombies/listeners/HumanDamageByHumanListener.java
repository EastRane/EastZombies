package me.eastrane.eastzombies.listeners;

import me.eastrane.eastzombies.EastZombies;
import me.eastrane.eastzombies.listeners.core.BaseListener;
import me.eastrane.eastzombies.storages.core.BaseStorage;
import me.eastrane.eastzombies.utilities.ConfigProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HumanDamageByHumanListener extends BaseListener implements Listener {
    private ConfigProvider configProvider;
    private final BaseStorage baseStorage;

    public HumanDamageByHumanListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        baseStorage = plugin.getBaseStorage();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        configProvider = plugin.getConfigProvider();
        return !configProvider.isFriendlyFireHumans();
    }

    @EventHandler
    private void onPlayerDealDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (!(damager instanceof Player) || !(entity instanceof Player)) return;
        if (baseStorage.isZombie(((Player) damager).getPlayer()) || baseStorage.isZombie(((Player) entity).getPlayer())) return;
        event.setCancelled(true);
    }
}
