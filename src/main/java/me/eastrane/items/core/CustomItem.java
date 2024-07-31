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

    protected abstract ItemStack createItemStack();

    protected abstract ShapedRecipe createRecipe();

    public void register() {
        this.recipe = createRecipe();
        plugin.getServer().addRecipe(recipe);
        plugin.getDebugManager().sendInfo(this.getClass().getSimpleName() + " was registered.");
        isRegistered = true;
    }

    public void unregister() {
        plugin.getServer().removeRecipe(this.getKey());
        plugin.getDebugManager().sendInfo(this.getClass().getSimpleName() + " was unregistered.");
        isRegistered = false;

    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public NamespacedKey getKey() {
        // eastzombies:zombie_compass
        return key;
    }

    public String getIdentifier() {
        // zombie_compass
        return key.getKey();
    }

    public CustomItemType getType() {
        return type;
    }

    protected abstract boolean shouldRegister(long[] worldTime);

    public boolean isRegistered() {
        return isRegistered;
    }
}
