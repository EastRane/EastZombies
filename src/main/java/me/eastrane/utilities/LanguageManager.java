package me.eastrane.utilities;

import me.eastrane.EastZombies;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LanguageManager {
    private final DebugManager debugManager;
    private final EastZombies plugin;
    private String language, prefix;

    public LanguageManager(EastZombies plugin) {
        this.plugin = plugin;
        debugManager = plugin.getDebugManager();
        loadLanguages();
    }

    public void loadLanguages() {
        language = plugin.getConfigManager().getLanguage();
        String languageFolder = plugin.getDataFolder() + File.separator + "languages";
        File folder = new File(languageFolder);
        String[] defaultLanguages = {"ru_RU"};
        for (String lang : defaultLanguages) {
            File languageFile = new File(languageFolder, lang + ".yml");
            if (!languageFile.exists()) {
                plugin.saveResource("languages" + File.separator + lang + ".yml", false);
            }
        }
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                checkLanguageConfig(file);
            }
        }
        File selectedLanguageFile = new File(languageFolder, language + ".yml");
        if (selectedLanguageFile.exists()) {
            YamlConfiguration.loadConfiguration(selectedLanguageFile);
        } else {
            debugManager.sendWarning("Selected language file " + selectedLanguageFile.getName() + " does not exist.");
        }
        prefix = Colorize(getTranslation("main.prefix"));
        debugManager.sendInfo("Using language " + language + ".", true);
    }

    private void checkLanguageConfig(File langFile) {
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(langFile);
            // not ru_RU, but en_US at final plugin development
            InputStream defaultConfigStream = plugin.getResource("languages/ru_RU.yml");
            if (defaultConfigStream == null) {
                debugManager.sendWarning("Default resource language file is missing.");
                return;
            }
            FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8));
            boolean configUpdated = false;
            for (String key : defaultConfig.getKeys(true)) {
                if (!config.isSet(key)) {
                    config.set(key, defaultConfig.get(key));
                    configUpdated = true;
                }
            }
            for (String key : config.getKeys(true)) {
                if (!defaultConfig.isSet(key)) {
                    config.set(key, null);
                    configUpdated = true;
                }
            }
            if (configUpdated) {
                config.setDefaults(defaultConfig);
                config.save(langFile);
                debugManager.sendWarning("Your language file contains differences in the set of options compared to the default version. It was corrected.");
            }
        } catch (Exception e) {
            debugManager.sendException(e);
        }
    }

    /**
     * Sends a message to the player by adding a prefix to it and applying color codes.
     */
    public void sendMessage(CommandSender sender, String translationKey, Object... args) {
        String translation = getTranslation(translationKey);
        if (translation != null) {
            String message = String.format(translation, args);
            sender.sendMessage(Colorize(prefix + message));
        } else {
            debugManager.sendWarning("Translation was not found for key: " + translationKey);
        }
    }

    /**
     * Backup method for sending colour messages without involving language files.
     */
    public void sendMessageManual(CommandSender sender, String message) {
        sender.sendMessage(Colorize(prefix + message));
    }

    /**
     * Sends a message to all players by adding a prefix to it and applying color codes.
     */
    public void broadcastMessage(String translationKey, Object... args) {
        String translation = getTranslation(translationKey);
        if (translation != null) {
            String message = String.format(translation, args);
            plugin.getServer().broadcastMessage(Colorize(prefix + message));
        } else {
            debugManager.sendWarning("Translation was not found for key: " + translationKey);
        }
    }

    /**
     * Finds the line corresponding to the key in the language file.
     */
    public String getTranslation(String translationKey) {
        File languageFile = new File(plugin.getDataFolder(), "languages" + File.separator + language + ".yml");
        if (!languageFile.exists()) {
            debugManager.sendWarning("Language file not found: " + language);
        }
        YamlConfiguration langFileName = YamlConfiguration.loadConfiguration(languageFile);
        if (langFileName.isSet(translationKey)) {
            return langFileName.getString(translationKey);
        }
        debugManager.sendWarning("Translation not found for key: " + translationKey);
        return null;
    }

    public String Colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}