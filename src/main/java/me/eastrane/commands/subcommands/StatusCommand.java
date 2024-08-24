package me.eastrane.commands.subcommands;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.HandlerManager;
import me.eastrane.items.core.CustomItemType;
import me.eastrane.items.core.ItemManager;
import me.eastrane.listeners.core.ListenerManager;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class StatusCommand extends SubCommand {
    private final ConfigProvider configProvider;
    private final LanguageProvider languageProvider;
    private final DataManager dataManager;
    private final ListenerManager listenerManager;
    private final HandlerManager handlerManager;
    private final ItemManager itemManager;

    public StatusCommand(EastZombies plugin) {
        this.plugin = plugin;
        configProvider = plugin.getConfigProvider();
        languageProvider = plugin.getLanguageProvider();
        dataManager = plugin.getDataManager();
        listenerManager = plugin.getListenerManager();
        handlerManager = plugin.getHandlerManager();
        itemManager = plugin.getItemManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length != 1) {
            languageProvider.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        languageProvider.sendMessageManual(sender, getStatus());

    }

    protected String getStatus() {
        StringBuilder status = new StringBuilder();

        boolean isTarget = listenerManager.getListener("EntityTargetListener").isRegistered();
        boolean isFlesh = listenerManager.getListener("ItemConsumeListener").isRegistered();
        boolean isSunBurn = handlerManager.getHandler("SunBurnHandler").isRegistered();
        boolean isHunger = listenerManager.getListener("EntityDamageByEntityListener").isRegistered();
        boolean isGolems = handlerManager.getHandler("IronGolemAttackHandler").isRegistered();
        boolean isZombieCompass = itemManager.getCustomItem(CustomItemType.ZOMBIE_COMPASS).isRegistered();

        long gameDay = plugin.getServer().getWorlds().get(0).getFullTime() / 24000L;
        status.append("\n");
        status.append(ChatColor.DARK_GREEN).append("Target: ").append(isTarget ? ChatColor.GREEN : ChatColor.RED).append(isTarget)
                .append(ChatColor.DARK_GREEN).append(" (Day " + gameDay).append("/" + configProvider.getTargetDay() + ")").append("\n");
        status.append(ChatColor.DARK_GREEN).append("Flesh: ").append(isFlesh ? ChatColor.GREEN : ChatColor.RED).append(isFlesh)
                .append(ChatColor.DARK_GREEN).append(" (Day " + gameDay).append("/" + configProvider.getFleshDay() + ")").append("\n");
        status.append(ChatColor.DARK_GREEN).append("SunBurn: ").append(isSunBurn ? ChatColor.GREEN : ChatColor.RED).append(isSunBurn)
                .append(ChatColor.DARK_GREEN).append(" (Day " + gameDay).append("/" + configProvider.getSunBurnDay() + ")").append("\n");
        status.append(ChatColor.DARK_GREEN).append("Hunger: ").append(isHunger ? ChatColor.GREEN : ChatColor.RED).append(isHunger)
                .append(ChatColor.DARK_GREEN).append(" (Day " + gameDay).append("/" + configProvider.getHungerDay() + ")").append("\n");
        status.append(ChatColor.DARK_GREEN).append("Golems: ").append(isGolems ? ChatColor.GREEN : ChatColor.RED).append(isGolems)
                .append(ChatColor.DARK_GREEN).append(" (Day " + gameDay).append("/" + configProvider.getGolemsDay() + ")").append("\n");
        status.append(ChatColor.DARK_GREEN).append("ZombieCompass: ").append(isZombieCompass ? ChatColor.GREEN : ChatColor.RED).append(isZombieCompass)
                .append(ChatColor.DARK_GREEN).append(" (Day " + gameDay).append("/" + configProvider.getZombieCompassDay() + ")");

        return status.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
