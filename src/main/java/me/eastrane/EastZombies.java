package me.eastrane;

import me.eastrane.commands.MainCommand;
import me.eastrane.handlers.core.HandlerManager;
import me.eastrane.items.core.CooldownManager;
import me.eastrane.items.core.ItemManager;
import me.eastrane.handlers.*;
import me.eastrane.listeners.core.ListenerManager;
import me.eastrane.storages.YamlStorage;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class EastZombies extends JavaPlugin {
    private ConfigProvider configProvider;
    private LanguageProvider languageProvider;
    private DebugProvider debugProvider;
    private BaseStorage baseStorage;
    private PlayerManager playerManager;
    private ListenerManager listenerManager;
    private HandlerManager handlerManager;
    private CooldownManager cooldownManager;
    private ItemManager itemManager;

    private FeaturesManager featuresManager;

    private SkinsHandler skinsHandler;
    private TeamHandler teamHandler;
    private EffectsHandler effectsHandler;
    private VoiceHandler voiceHandler;

    @Override
    public void onEnable() {
        featuresManager = new FeaturesManager(this);
        registerManagers();
        MainCommand mainCommand = new MainCommand(this);
        this.getCommand("eastzombies").setExecutor(mainCommand);
        this.getCommand("eastzombies").setTabCompleter(mainCommand);
        new MetricsProvider(this);
        new UpdateProvider(this);
    }

    private void registerManagers() {
        getConfigProvider();
        getLanguageProvider();
        getDebugProvider();
        getBaseStorage();
        getPlayerManager();
        getFeaturesManager();
        getListenerManager();
        getHandlerManager();
        getCooldownManager();
        getItemManager();
    }

    public ConfigProvider getConfigProvider() {
        if (configProvider == null) {
            configProvider = new ConfigProvider(this);
        }
        return configProvider;
    }
    public LanguageProvider getLanguageProvider() {
        if (languageProvider == null) {
            languageProvider = new LanguageProvider(this);
        }
        return languageProvider;
    }
    public DebugProvider getDebugProvider() {
        if (debugProvider == null) {
            debugProvider = new DebugProvider(this);
        }
        return debugProvider;
    }
    public BaseStorage getBaseStorage() {
        if (baseStorage == null) {
            baseStorage = new YamlStorage(this);
        }
        return baseStorage;
    }
    public PlayerManager getPlayerManager() {
        if (playerManager == null) {
            playerManager = new PlayerManager(this);
        }
        return playerManager;
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

    public VoiceHandler getVoiceHandler() {
        if (voiceHandler == null) {
            voiceHandler = (VoiceHandler) getHandlerManager().getHandler("VoiceHandler");
        }
        return voiceHandler;
    }

    @Override
    public void onDisable() {
        getItemManager().unregisterRecipes();
    }
}