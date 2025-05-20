package me.eastrane.eastzombies.listeners;

import me.eastrane.eastzombies.EastZombies;
import me.eastrane.eastzombies.listeners.core.BaseListener;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        if (!plugin.getBaseStorage().isZombie(player)) {
            plugin.getEffectsHandler().clearEffects(player);
            player.setVisualFire(false);
        } else {
            plugin.getEffectsHandler().giveZombieEffects(player);
        }
    }
}
