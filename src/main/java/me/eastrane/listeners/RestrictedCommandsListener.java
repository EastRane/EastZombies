package me.eastrane.listeners;

import me.eastrane.EastZombies;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class RestrictedCommandsListener extends BaseListener implements Listener {
    private final ConfigProvider configProvider;
    private final LanguageProvider languageProvider;
    private final BaseStorage baseStorage;

    public RestrictedCommandsListener(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        configProvider = plugin.getConfigProvider();
        languageProvider = plugin.getLanguageProvider();
        baseStorage = plugin.getBaseStorage();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    @EventHandler
    public void onCommandInput(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (baseStorage.isZombie(player)) {
            List<String> restrictedCommands = (List<String>) configProvider.getRestrictedCommandsList();
            for (String command : restrictedCommands) {
                if (event.getMessage().toLowerCase().startsWith("/" + command.toLowerCase())) {
                    event.setCancelled(true);
                    languageProvider.sendMessage(player, "commands.errors.restricted_command");
                    return;
                }
            }
        }
    }

}
