package me.eastrane.listeners.core;

import me.eastrane.EastZombies;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;

public abstract class BaseListener implements Listener {
    protected EastZombies plugin;
    protected long[] worldTime;
    private boolean isRegistered = false;
    private final boolean isReloadable;

    public BaseListener(EastZombies plugin, boolean isReloadable) {
        this.plugin = plugin;
        this.isReloadable = isReloadable;
        this.worldTime = plugin.getFeaturesManager().getWorldTime();
        if (shouldRegister(worldTime)) {
            register();
        }
    }

    protected boolean register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getDebugManager().sendInfo(this.getClass().getSimpleName() + " was registered.");
        isRegistered = true;
        return true;
    }

    protected boolean unregister() {
        if (isReloadable) {
            HandlerList.unregisterAll(this);
            plugin.getDebugManager().sendInfo(this.getClass().getSimpleName() + " was unregistered.");
            isRegistered = false;
            return true;
        }
        return false;
    }

    protected abstract boolean shouldRegister(long[] worldTime);

    public boolean isRegistered() {
        return isRegistered;
    }
}
