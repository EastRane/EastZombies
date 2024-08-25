package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.handlers.EffectsHandler;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.storages.core.BaseStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EffectChangeListener extends BaseListener implements Listener {
    private final EastZombies plugin;
    private final BaseStorage baseStorage;
    private final EffectsHandler effectsHandler;

    public EffectChangeListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        this.plugin = plugin;
        baseStorage = plugin.getBaseStorage();
        effectsHandler = plugin.getEffectsHandler();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    @EventHandler
    public void onEffectChange(EntityPotionEffectEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (event.getAction() != EntityPotionEffectEvent.Action.CLEARED) return;
        Player player = (Player) event.getEntity();
        if (!baseStorage.isZombie(player)) return;

        EntityPotionEffectEvent.Cause cause = event.getCause();
        if (cause == EntityPotionEffectEvent.Cause.MILK) {
            event.setCancelled(true);
        } else if (cause == EntityPotionEffectEvent.Cause.TOTEM) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    effectsHandler.giveZombieEffects(player);
                }
            }.runTask(plugin);
        } else if (cause == EntityPotionEffectEvent.Cause.DEATH) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    effectsHandler.giveZombieEffects(player);
                }
            }.runTask(plugin);
        }
    }
}
