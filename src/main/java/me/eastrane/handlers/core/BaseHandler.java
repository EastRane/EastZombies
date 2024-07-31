package me.eastrane.handlers.core;

import me.eastrane.EastZombies;

public abstract class BaseHandler {
    protected EastZombies plugin;
    protected long[] worldTime;
    private boolean isRegistered = false;
    protected final boolean isReloadable;

    public BaseHandler(EastZombies plugin, boolean isReloadable) {
        this.plugin = plugin;
        this.worldTime = plugin.getFeaturesManager().getWorldTime();
        this.isReloadable = isReloadable;
        if (shouldRegister(worldTime)) {
            register();
        }
    }

    protected boolean register() {
        plugin.getDebugManager().sendInfo(this.getClass().getSimpleName() + " was registered.");
        isRegistered = true;
        return true;
    }

    protected boolean unregister() {
        if (isReloadable) {
            // This method (same as above) is expected to be overridden with logic for specific handler and calling super
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
