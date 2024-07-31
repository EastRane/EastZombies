package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuitListener extends BaseListener implements Listener {
    public JoinQuitListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getDataManager().isZombiePlayer(event.getPlayer())) {
            plugin.getEffectsHandler().clearEffects(event.getPlayer());
            event.getPlayer().setVisualFire(false);
        } else {
            plugin.getEffectsHandler().giveZombieEffects(event.getPlayer());
        }
    }
}
