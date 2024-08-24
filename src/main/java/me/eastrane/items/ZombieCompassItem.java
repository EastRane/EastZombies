package me.eastrane.items;

import me.eastrane.EastZombies;
import me.eastrane.items.core.CustomItem;
import me.eastrane.items.core.CustomItemType;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZombieCompassItem extends CustomItem {
    private ConfigProvider configProvider;

    public ZombieCompassItem(EastZombies plugin) {
        super(plugin, CustomItemType.ZOMBIE_COMPASS);
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        configProvider = plugin.getConfigProvider();
        return configProvider.isZombieCompass() &&
                (worldTime[0] > configProvider.getZombieCompassDay()) ||
                (worldTime[0] == configProvider.getZombieCompassDay() && !configProvider.isZombieCompassAtNight()) ||
                (worldTime[0] == configProvider.getZombieCompassDay() && configProvider.isZombieCompassAtNight() && worldTime[1] >= 13000);
    }

    @Override
    protected ItemStack createItemStack() {
        LanguageProvider languageProvider = plugin.getLanguageProvider();
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, getIdentifier());
        itemMeta.setDisplayName(languageProvider.Colorize(languageProvider.getTranslation("buffs.zombie_compass.name")));
        itemMeta.setLore(List.of(languageProvider.Colorize(languageProvider.getTranslation("buffs.zombie_compass.lore"))));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    protected ShapedRecipe createRecipe() {
        List<String> recipeRows = plugin.getConfigProvider().getZombieCompassRecipe();
        Map<String, Character> materialToLetter = new HashMap<>();
        Map<Character, Material> letterToMaterial = new HashMap<>();
        char letter = 'A';
        for (String row : recipeRows) {
            String[] materials = row.split(" ");
            for (String material : materials) {
                if (!materialToLetter.containsKey(material)) {
                    materialToLetter.put(material, letter);
                    letterToMaterial.put(letter, Material.valueOf(material));
                    letter++;
                }
            }
        }
        String[] shape = new String[recipeRows.size()];
        for (int i = 0; i < recipeRows.size(); i++) {
            StringBuilder shapeRow = new StringBuilder();
            String[] materials = recipeRows.get(i).split(" ");
            for (String material : materials) {
                shapeRow.append(materialToLetter.get(material));
            }
            shape[i] = shapeRow.toString();
        }
        ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
        recipe.shape(shape);
        for (Map.Entry<Character, Material> entry : letterToMaterial.entrySet()) {
            recipe.setIngredient(entry.getKey(), entry.getValue());
        }
        return recipe;
    }

}
