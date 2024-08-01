package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    protected EastZombies plugin;

    public abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public boolean hasPermission(CommandSender sender) {
        String permission = "eastzombies." + this.getClass().getSimpleName().toLowerCase().replace("command", "");
        if (sender.hasPermission(permission) || sender.hasPermission("eastzombies.admin")) {
            return true;
        }
        plugin.getLanguageManager().sendMessage(sender, "commands.errors.no_permission");
        return false;
    }
}
