package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    protected EastZombies plugin;

    public abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}
