package me.eastrane.utilities;

import me.eastrane.EastZombies;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConfigManager {
    private final EastZombies plugin;
    FileConfiguration config;
    private String language;
    private boolean debugConsole, debugFile, dropHead, changeSkin, dropFlesh, resetRespawnOnFirstDeath, effects;
    private boolean target, targetAtNight, flesh, fleshAtNight, sunburn, sunburnAtNight, hunger, hungerAtNight, golems, golemsAtNight, zombieCompass, zombieCompassAtNight;
    private int targetDay, fleshDay, sunburnDay, hungerDay, golemsDay, zombieCompassDay;
    private int dropFleshAmount, sunburnDamage, hungerDuration, zombieCompassCooldown;
    private List<String> zombieCompassRecipe;
    private List<Map<?, ?>> effectsList;
    private List<?> restrictedCommandsList;

    public ConfigManager(EastZombies plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        checkConfig();
        loadConfig();
    }

    /**
     * Loads the configuration settings from the YAML file.
     */
    public void loadConfig() {
        config = plugin.getConfig();
        language = config.getString("language");
        debugConsole = config.getBoolean("debug.console");
        debugFile = config.getBoolean("debug.file");
        dropHead = config.getBoolean("player.drop_head");
        changeSkin = config.getBoolean("player.change_skin");
        dropFlesh = config.getBoolean("player.flesh.drop_flesh");
        dropFleshAmount = config.getInt("player.flesh.amount");
        resetRespawnOnFirstDeath = config.getBoolean("player.reset_respawn_on_first_death");
        effects = plugin.getConfig().getBoolean("player.effects.enabled");
        effectsList = plugin.getConfig().getMapList("player.effects.list");
        restrictedCommandsList = plugin.getConfig().getList("player.restricted_commands");

        target = config.getBoolean("features.target.enabled");
        targetDay = config.getInt("features.target.start_day");
        targetAtNight = config.getBoolean("features.target.at_night");
        flesh = config.getBoolean("features.flesh.enabled");
        fleshDay = config.getInt("features.flesh.start_day");
        fleshAtNight = config.getBoolean("features.flesh.at_night");
        sunburn = config.getBoolean("features.sun_burn.enabled");
        sunburnDay = config.getInt("features.sun_burn.start_day");
        sunburnAtNight = config.getBoolean("features.sun_burn.at_night");
        sunburnDamage = config.getInt("features.sun_burn.damage");
        hunger = config.getBoolean("features.hunger.enabled");
        hungerDay = config.getInt("features.hunger.start_day");
        hungerAtNight = config.getBoolean("features.hunger.at_night");
        hungerDuration = config.getInt("features.hunger.duration");
        golems = config.getBoolean("features.golems.enabled");
        golemsDay = config.getInt("features.golems.start_day");
        golemsAtNight = config.getBoolean("features.golems.at_night");
        zombieCompass = config.getBoolean("features.zombie_compass.enabled");
        zombieCompassDay = config.getInt("features.zombie_compass.start_day");
        zombieCompassAtNight = config.getBoolean("features.zombie_compass.at_night");
        zombieCompassCooldown = config.getInt("features.zombie_compass.cooldown");
        zombieCompassRecipe = new ArrayList<>();
        zombieCompassRecipe.add( config.getString("features.zombie_compass.recipe.first_row"));
        zombieCompassRecipe.add(config.getString("features.zombie_compass.recipe.second_row"));
        zombieCompassRecipe.add(config.getString("features.zombie_compass.recipe.third_row"));
    }

    /**
     * Reloads the configuration settings from the YAML file.
     * It also checks for any differences between the current configuration and the default configuration.
     */
    public void reloadConfig() {
        plugin.getDebugManager().sendInfo("Reloading configuration file...", true);
        checkConfig();
        plugin.reloadConfig();
        loadConfig();
        plugin.getLanguageManager().loadLanguages();
        plugin.getFeaturesManager().reactivateEvents();
    }

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
                plugin.getDebugManager().sendWarning("Default resource configuration file is missing.");
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
                plugin.getDebugManager().sendWarning("Your configuration file contains differences in the set of options compared to the default version. It was corrected.");
            }
        } catch (Exception e) {
            plugin.getDebugManager().sendException(e);
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
            plugin.getDebugManager().sendException(e);
        }
    }

    public String getLanguage() {
        return language;
    }
    public boolean isDebugConsole() {
        return debugConsole;
    }
    public boolean isDebugFile() {
        return debugFile;
    }
    public boolean isDropHead() {
        return dropHead;
    }
    public boolean isChangeSkin() {
        return changeSkin;
    }
    public void setChangeSkin(boolean state) {
        changeSkin = state;
    }
    public boolean isDropFlesh() {
        return dropFlesh;
    }
    public int getDropFleshAmount() {
        return dropFleshAmount;
    }
    public boolean isResetRespawnOnFirstDeath() {
        return resetRespawnOnFirstDeath;
    }
    public boolean isEffects() {
        return effects;
    }
    public List<Map<?, ?>> getEffectsList() {
        return effectsList;
    }
    public List<?> getRestrictedCommandsList() { return restrictedCommandsList; }

    public boolean isTarget() {
        return target;
    }
    public int getTargetDay() {
        return targetDay;
    }
    public boolean isTargetAtNight() {
        return targetAtNight;
    }
    public boolean isFlesh() {
        return flesh;
    }
    public int getFleshDay() {
        return fleshDay;
    }
    public boolean isFleshAtNight() {
        return fleshAtNight;
    }
    public boolean isSunburn() {
        return sunburn;
    }
    public int getSunburnDay() {
        return sunburnDay;
    }
    public boolean isSunburnAtNight() {
        return sunburnAtNight;
    }
    public int getSunburnDamage() {
        return sunburnDamage;
    }
    public boolean isHunger() {
        return hunger;
    }
    public int getHungerDay() {
        return hungerDay;
    }
    public boolean isHungerAtNight() {
        return hungerAtNight;
    }
    public int getHungerDuration() {
        return hungerDuration;
    }
    public boolean isGolems() {
        return golems;
    }
    public int getGolemsDay() {
        return golemsDay;
    }
    public boolean isGolemsAtNight() {
        return golemsAtNight;
    }
    public boolean isZombieCompass() {
        return zombieCompass;
    }
    public int getZombieCompassDay() {
        return zombieCompassDay;
    }
    public boolean isZombieCompassAtNight() {
        return zombieCompassAtNight;
    }
    public List<String> getZombieCompassRecipe() { return zombieCompassRecipe; }
    public int getZombieCompassCooldown() {
        return zombieCompassCooldown;
    }

}
