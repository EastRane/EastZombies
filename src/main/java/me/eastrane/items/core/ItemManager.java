package me.eastrane.items.core;

import me.eastrane.EastZombies;
import me.eastrane.items.ZombieCompassItem;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private final EastZombies plugin;
    private final Map<CustomItemType, CustomItem> customItems = new HashMap<>();

    public ItemManager(EastZombies plugin) {
        this.plugin = plugin;
        registerCustomItems();
    }

    private void registerCustomItems() {
        registerCustomItem(new ZombieCompassItem(plugin));
    }

    private void registerCustomItem(CustomItem customItem) {
        customItems.put(customItem.getType(), customItem);
    }

    /**
     * Retrieves a custom item by its type.
     *
     * @param type The type of the custom item.
     * @return The custom item or null if not found.
     */
    public CustomItem getCustomItem(CustomItemType type) {
        return customItems.get(type);
    }

    /**
     * Unregisters all custom item recipes.
     */
    public void unregisterRecipes() {
        for (CustomItem item : customItems.values()) {
            item.unregister();
        }
    }

    /**
     * Rechecks the registration status of all custom items.
     *
     * @param worldTime The current world time.
     * @return A map containing the names of custom items and their registration status changes.
     *         If a item's registration status does not change, it is not included in the map.
     */
    public Map<String, Boolean> recheckItems(long[] worldTime) {
        Map<String, Boolean> changes = new HashMap<>();

        for (CustomItem customItem : customItems.values()) {
            String itemName = customItem.getType().name();
            if (customItem.shouldRegister(worldTime)) {
                if (!customItem.isRegistered()) {
                    customItem.register();
                    changes.put(itemName, true);
                }
            } else {
                if (customItem.isRegistered()) {
                    customItem.unregister();
                    changes.put(itemName, false);
                }
            }
        }

        return changes;
    }
}
