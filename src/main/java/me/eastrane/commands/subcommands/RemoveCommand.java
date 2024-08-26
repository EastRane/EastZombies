package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RemoveCommand extends SubCommand {
    private final BaseStorage baseStorage;
    private final LanguageProvider languageProvider;

    public RemoveCommand(EastZombies plugin) {
        this.plugin = plugin;
        baseStorage = plugin.getBaseStorage();
        languageProvider = plugin.getLanguageProvider();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length < 2) {
            languageProvider.sendMessage(sender, "commands.errors.too_few_arguments");
            return;
        } else if (args.length > 2) {
            languageProvider.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        UUID player = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
        if (!baseStorage.isZombie(plugin.getServer().getOfflinePlayer(player))) {
            languageProvider.sendMessage(sender, "commands.remove.zombie_not_found");
            return;
        }
        plugin.getPlayerManager().removeZombie(player);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                list.add(p.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
