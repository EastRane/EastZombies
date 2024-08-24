package me.eastrane.items.core;

import me.eastrane.EastZombies;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public abstract class CustomItem {
    protected EastZombies plugin;
    protected NamespacedKey key;
    protected ItemStack itemStack;
    protected ShapedRecipe recipe;
    protected CustomItemType type;
    protected long[] worldTime;
    private boolean isRegistered = false;

    public CustomItem(EastZombies plugin, CustomItemType type) {
        this.plugin = plugin;
        this.type = type;
        this.key = new NamespacedKey(plugin, type.getIdentifier());
        this.itemStack = createItemStack();
        this.worldTime = plugin.getFeaturesManager().getWorldTime();
        if (shouldRegister(worldTime)) {
            register();
        }
    }

    /**
     * Creates and returns the ItemStack for the custom item.
     * This method should be implemented by subclasses.
     *
     * @return The ItemStack for the custom item.
     */
    protected abstract ItemStack createItemStack();

    /**
     * Creates and returns the ShapedRecipe for the custom item.
     * This method should be implemented by subclasses.
     *
     * @return The ShapedRecipe for the custom item.
     */
    protected abstract ShapedRecipe createRecipe();

    /**
     * Registers the custom item by creating and adding its recipe to the server.
     */
    public void register() {
        this.recipe = createRecipe();
        plugin.getServer().addRecipe(recipe);
        plugin.getDebugProvider().sendInfo(this.getClass().getSimpleName() + " was registered.");
        isRegistered = true;
    }

    /**
     * Unregisters the custom item by removing its recipe from the server.
     */
    public void unregister() {
        plugin.getServer().removeRecipe(this.getKey());
        plugin.getDebugProvider().sendInfo(this.getClass().getSimpleName() + " was unregistered.");
        isRegistered = false;

    }

    /**
     * Returns the ItemStack for the custom item.
     *
     * @return The ItemStack for the custom item.
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Returns the ShapedRecipe for the custom item.
     *
     * @return The ShapedRecipe for the custom item.
     */
    public ShapedRecipe getRecipe() {
        return recipe;
    }

    /**
     * Returns the NamespacedKey for the custom item.
     *
     * @return The NamespacedKey for the custom item.
     */
    public NamespacedKey getKey() {
        // eastzombies:zombie_compass
        return key;
    }
    /**
     * Returns the identifier for the custom item.
     *
     * @return The identifier for the custom item.
     */
    public String getIdentifier() {
        // zombie_compass
        return key.getKey();
    }

    /**
     * Returns the type of the custom item.
     *
     * @return The type of the custom item.
     */
    public CustomItemType getType() {
        return type;
    }

    /**
     * Determines whether the custom item should be registered.
     * This method should be implemented by subclasses.
     *
     * @param worldTime The current world time.
     * @return True if the custom item should be registered, false otherwise.
     */
    protected abstract boolean shouldRegister(long[] worldTime);

    /**
     * Returns whether the custom item is currently registered.
     *
     * @return True if the custom item is registered, false otherwise.
     */
    public boolean isRegistered() {
        return isRegistered;
    }
}
