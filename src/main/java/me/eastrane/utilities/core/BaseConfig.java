package me.eastrane.utilities.core;

import me.eastrane.EastZombies;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class BaseConfig {
    protected final EastZombies plugin;
    protected FileConfiguration config;

    public BaseConfig(EastZombies plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        checkConfig();
        loadConfig();
    }

    public void loadConfig() {
        config = plugin.getConfig();
        loadCustomConfig();
    }

    public abstract void loadCustomConfig();

    /**
     * Reloads the configuration settings from the YAML file.
     * It also checks for any differences between the current configuration and the default configuration.
     */
    public void reloadConfig() {
        plugin.getDebugProvider().sendInfo("Reloading configuration file...", true);
        checkConfig();
        plugin.reloadConfig();
        loadConfig();
        plugin.getLanguageProvider().loadLanguages();
        reloadCustomConfig();
    }

    protected abstract void reloadCustomConfig();

    /**
     * Checks for any differences between the current configuration and the default configuration.
     * If any differences are found, it updates the current configuration to match the default configuration.
     */
    public void checkConfig() {
        try {
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                plugin.saveDefaultConfig();
                return;
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            InputStream defaultConfigStream = plugin.getResource("config.yml");
            if (defaultConfigStream == null) {
                plugin.getDebugProvider().sendWarning("Default resource configuration file is missing.");
                return;
            }
            FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8));
            boolean configUpdated = false;
            for (String key : defaultConfig.getKeys(true)) {
                if (defaultConfig.isBoolean(key) && !config.isBoolean(key)) {
                    config.set(key, defaultConfig.getBoolean(key));
                    configUpdated = true;
                } else if (defaultConfig.isInt(key) && !config.isInt(key)) {
                    config.set(key, defaultConfig.getInt(key));
                    configUpdated = true;
                } else if (defaultConfig.isString(key) && !config.isString(key)) {
                    config.set(key, defaultConfig.getString(key));
                    configUpdated = true;
                } else if (defaultConfig.isList(key) && !config.isList(key)) {
                    config.set(key, defaultConfig.getList(key));
                    configUpdated = true;
                }
            }
            for (String key : config.getKeys(true)) {
                if (!defaultConfig.contains(key)) {
                    config.set(key, null);
                    configUpdated = true;
                }
            }
            if (configUpdated) {
                config.setDefaults(defaultConfig);
                YamlConfigurationOptions options = (YamlConfigurationOptions) config.options();
                options.parseComments(true).copyDefaults(true).width(500);
                config.loadFromString(config.saveToString());
                for (String key : defaultConfig.getKeys(true)) {
                    config.setComments(key, defaultConfig.getComments(key));
                }
                config.save(configFile);
                plugin.getDebugProvider().sendWarning("Your configuration file contains differences in the set of options compared to the default version. It was corrected.");
            }
        } catch (Exception e) {
            plugin.getDebugProvider().sendException(e);
        }
    }

    /**
     * Saves the current configuration settings to the YAML file.
     */
    public void saveConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getDebugProvider().sendException(e);
        }
    }
}
