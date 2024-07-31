package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class RestrictedCommandsListener extends BaseListener implements Listener {
    private final ConfigManager configManager;
    private final LanguageManager languageManager;
    private final DataManager dataManager;

    public RestrictedCommandsListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        configManager = plugin.getConfigManager();
        languageManager = plugin.getLanguageManager();
        dataManager = plugin.getDataManager();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    @EventHandler
    public void onCommandInput(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (dataManager.isZombiePlayer(player)) {
            List<String> restrictedCommands = (List<String>) configManager.getRestrictedCommandsList();
            for (String command : restrictedCommands) {
                if (event.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())) {
                    event.setCancelled(true);
                    languageManager.sendMessage(player, "commands.errors.restricted_command");
                    return;
                }
            }
        }
    }

}
