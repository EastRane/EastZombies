package me.eastrane.utilities;

import me.eastrane.EastZombies;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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

    /**
     * Loads and sets up the language files for the plugin.
     */
    public void loadLanguages() {
        language = plugin.getConfigManager().getLanguage();
        String languageFolder = plugin.getDataFolder() + File.separator + "languages";
        File folder = new File(languageFolder);
        String[] defaultLanguages = {"en_US", "ru_RU", "uk_UA"};
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

    /**
     * Checks and updates the language configuration file to ensure it matches the default version.
     *
     * @param langFile The language configuration file to be checked.
     */
    private void checkLanguageConfig(File langFile) {
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(langFile);
            // not ru_RU, but en_US at final plugin development
            InputStream defaultConfigStream = plugin.getResource("languages/en_US.yml");
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
     *
     * @param sender         The CommandSender object representing the player who will receive the message.
     * @param translationKey The key of the translation in the language file to be used.
     * @param args           Variable arguments to be used in the formatted translation string.
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
     * Backup method for sending color messages without involving language files.
     *
     * @param sender  The CommandSender object representing the player who will receive the message.
     * @param message The message to be sent.
     */
    public void sendMessageManual(CommandSender sender, String message) {
        sender.sendMessage(Colorize(prefix + message));
    }

    /**
     * Sends a message to all players by adding a prefix to it and applying color codes.
     *
     * @param translationKey The key of the translation in the language file to be used.
     * @param args           Variable arguments to be used in the formatted translation string.
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
     * Sends a title to all players by adding a prefix to it and applying color codes.
     *
     * @param titleKey The key of the title translation in the language file to be used.
     * @param subtitleKey The key of the subtitle translation in the language file to be used. Can be null if no subtitle is desired.
     * @param fadeIn The time in ticks for the title to fade in.
     * @param stay The time in ticks for the title to stay on screen.
     * @param fadeOut The time in ticks for the title to fade out.
     * @param args Variable arguments to be used in formatted translation string.
     */
    public void broadcastTitle(String titleKey, String subtitleKey, int fadeIn, int stay, int fadeOut, Object... args) {
        String title = getTranslation(titleKey);
        String subtitle = subtitleKey != null ? getTranslation(subtitleKey) : null;

        if (title != null) {
            title = String.format(title, args);
            title = Colorize(title);

            if (subtitle != null) {
                subtitle = String.format(subtitle, args);
                subtitle = Colorize(subtitle);
            }

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        } else {
            debugManager.sendWarning("Translation was not found for title key: " + titleKey);
        }
    }

    /**
     * Finds the line corresponding to the key in the language file.
     *
     * @param translationKey The key of the translation in the language file to be found.
     * @return The translation string corresponding to the key, or null if not found.
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
        return null;
    }

    /**
     * Applies color codes to the provided text.
     *
     * @param text The text to be colorized.
     * @return The colorized text.
     */
    public String Colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}