package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.items.core.CustomItemType;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class FeaturesManager {
    private final EastZombies plugin;
    private final LanguageProvider languageProvider;
    private final World world;
    private long lastDay = -1;

    public FeaturesManager(EastZombies plugin) {
        this.plugin = plugin;
        languageProvider = plugin.getLanguageProvider();
        world = plugin.getServer().getWorlds().get(0);
        new BukkitRunnable() {
            @Override
            public void run() {
                long[] worldTime = getWorldTime();
                long currentDay = worldTime[0];
                Map<String, Boolean> listenerChanges = plugin.getListenerManager().recheckListeners(worldTime);
                Map<String, Boolean> handlerChanges = plugin.getHandlerManager().recheckHandlers(worldTime);
                Map<String, Boolean> itemChanges = plugin.getItemManager().recheckItems(worldTime);
                if (Boolean.TRUE.equals(listenerChanges.get("EntityTargetListener"))) {
                    languageProvider.broadcastMessage("buffs.broadcast.no_target");
                }

                if (Boolean.TRUE.equals(listenerChanges.get("ItemConsumeListener"))) {
                    languageProvider.broadcastMessage("buffs.broadcast.flesh_no_hunger");
                }

                if (Boolean.TRUE.equals(handlerChanges.get("SunBurnHandler"))) {
                    languageProvider.broadcastMessage("debuffs.broadcast.sun_burn");
                }

                if (Boolean.TRUE.equals(listenerChanges.get("EntityDamageByEntityListener"))) {
                    languageProvider.broadcastMessage("buffs.broadcast.hunger_attack");
                }

                if (Boolean.TRUE.equals(handlerChanges.get("IronGolemAttackHandler"))) {
                    languageProvider.broadcastMessage("debuffs.broadcast.golems");
                }

                if (Boolean.TRUE.equals(itemChanges.get(CustomItemType.ZOMBIE_COMPASS.toString()))) {
                    languageProvider.broadcastMessage("buffs.broadcast.zombie_compass");
                }

                if (plugin.getConfigProvider().isBroadcastDay() && currentDay != lastDay) {
                    languageProvider.broadcastTitle("broadcasts.day", null, 20, 60, 20, currentDay + 1);
                    lastDay = currentDay;
                }

            }
        }.runTaskTimer(plugin, 0L, 60L);
    }

    public long[] getWorldTime() {
        long ticks = world.getFullTime();
        long days = ticks / 24000L;
        long timeOfDay = ticks % 24000L;
        return new long[]{days, timeOfDay};
    }

    public void reactivateEvents() {
        plugin.getListenerManager().unregisterListeners();
        plugin.getHandlerManager().unregisterHandlers();
        plugin.getItemManager().unregisterRecipes();
        plugin.getListenerManager().recheckListeners(getWorldTime());
        plugin.getHandlerManager().recheckHandlers(getWorldTime());
        plugin.getItemManager().recheckItems(getWorldTime());
    }
}
