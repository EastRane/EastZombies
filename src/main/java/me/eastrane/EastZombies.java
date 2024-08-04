package me.eastrane;

import me.eastrane.commands.MainCommand;
import me.eastrane.handlers.core.HandlerManager;
import me.eastrane.items.core.CooldownManager;
import me.eastrane.items.core.ItemManager;
import me.eastrane.handlers.*;
import me.eastrane.listeners.core.ListenerManager;
import me.eastrane.utilities.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class EastZombies extends JavaPlugin {
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private DebugManager debugManager;
    private DataManager dataManager;
    private ListenerManager listenerManager;
    private HandlerManager handlerManager;
    private CooldownManager cooldownManager;
    private ItemManager itemManager;

    private FeaturesManager featuresManager;

    private SkinsHandler skinsHandler;
    private TeamHandler teamHandler;
    private EffectsHandler effectsHandler;

    @Override
    public void onEnable() {
        featuresManager = new FeaturesManager(this);
        registerManagers();
        new MetricsProvider(this);
        MainCommand mainCommand = new MainCommand(this);
        this.getCommand("eastzombies").setExecutor(mainCommand);
        this.getCommand("eastzombies").setTabCompleter(mainCommand);
    }

    private void registerManagers() {
        getConfigManager();
        getLanguageManager();
        getDebugManager();
        getDataManager();
        getFeaturesManager();
        getListenerManager();
        getHandlerManager();
        getCooldownManager();
        getItemManager();
    }

    public ConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = new ConfigManager(this);
        }
        return configManager;
    }
    public LanguageManager getLanguageManager() {
        if (languageManager == null) {
            languageManager = new LanguageManager(this);
        }
        return languageManager;
    }
    public DebugManager getDebugManager() {
        if (debugManager == null) {
            debugManager = new DebugManager(this);
        }
        return debugManager;
    }
    public DataManager getDataManager() {
        if (dataManager == null) {
            dataManager = new DataManager(this);
        }
        return dataManager;
    }
    public ListenerManager getListenerManager() {
        if (listenerManager == null) {
            listenerManager = new ListenerManager(this);
        }
        return listenerManager;
    }
    public HandlerManager getHandlerManager() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
    public CooldownManager getCooldownManager() {
        if (cooldownManager == null) {
            cooldownManager = new CooldownManager(this);
        }
        return cooldownManager;
    }
    public ItemManager getItemManager() {
        if (itemManager == null) {
            itemManager = new ItemManager(this);
        }
        return itemManager;
    }
    public FeaturesManager getFeaturesManager() {
        if (featuresManager == null) {
            featuresManager = new FeaturesManager(this);
        }
        return featuresManager;
    }

    public SkinsHandler getSkinsHandler() {
        if (skinsHandler == null) {
            skinsHandler = (SkinsHandler) getHandlerManager().getHandler("SkinsHandler");
        }
        return skinsHandler;
    }
    public TeamHandler getTeamHandler() {
        if (teamHandler == null) {
            teamHandler = (TeamHandler) getHandlerManager().getHandler("TeamHandler");
        }
        return teamHandler;
    }
    public EffectsHandler getEffectsHandler() {
        if (effectsHandler == null) {
            effectsHandler = (EffectsHandler) getHandlerManager().getHandler("EffectsHandler");
        }
        return effectsHandler;
    }

    @Override
    public void onDisable() {
        getItemManager().unregisterRecipes();
    }
}