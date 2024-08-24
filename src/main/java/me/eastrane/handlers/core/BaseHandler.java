package me.eastrane.handlers.core;

import me.eastrane.EastZombies;

public abstract class BaseHandler {
    protected EastZombies plugin;
    protected long[] worldTime;
    private boolean isRegistered = false;
    protected final boolean isReloadable;

    /**
     * Constructs a new BaseHandler instance.
     *
     * @param plugin The main plugin instance.
     * @param isReloadable Indicates whether the handler can be unregistered during a plugin reload.
     */
    public BaseHandler(EastZombies plugin, boolean isReloadable) {
        this.plugin = plugin;
        this.worldTime = plugin.getFeaturesManager().getWorldTime();
        this.isReloadable = isReloadable;
        if (shouldRegister(worldTime)) {
            register();
        }
    }

    /**
     * Registers the handler.
     * It is expected to be overridden by subclasses with logic for specific handler and calling super.
     *
     * @return True if the handler was successfully registered, false otherwise.
     */
    protected boolean register() {
        plugin.getDebugProvider().sendInfo(this.getClass().getSimpleName() + " was registered.");
        isRegistered = true;
        return true;
    }

    /**
     * Unregisters the handler.
     * It is expected to be overridden by subclasses with logic for specific handler and calling super.
     *
     * @return True if the handler was successfully unregistered, false otherwise.
     *         If the handler is not reloadable, this method will always return false.
     */
    protected boolean unregister() {
        if (isReloadable) {
            plugin.getDebugProvider().sendInfo(this.getClass().getSimpleName() + " was unregistered.");
            isRegistered = false;
            return true;
        }
        return false;
    }

    /**
     * Checks whether the handler should be registered.
     *
     * @param worldTime The current world time.
     * @return True if the handler should be registered, false otherwise.
     */
    protected abstract boolean shouldRegister(long[] worldTime);

    /**
     * Checks whether the handler is currently registered.
     *
     * @return True if the handler is registered, false otherwise.
     */
    public boolean isRegistered() {
        return isRegistered;
    }
}
