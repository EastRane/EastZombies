package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DebugManager;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EffectsHandler extends BaseHandler {
    private final EastZombies plugin;
    private DebugManager debugManager;

    public EffectsHandler(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        this.plugin = plugin;
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    public void giveZombieEffects(Player player) {
        applyOrRemoveEffects(player, true);
    }

    public void clearEffects(Player player) {
        if (player != null) {
            applyOrRemoveEffects(player, false);
        }
    }

    private void applyOrRemoveEffects(Player player, boolean apply) {
        debugManager = plugin.getDebugManager();
        List<Map<?, ?>> effects = plugin.getConfigManager().getEffectsList();
        for (Map<?, ?> effect : effects) {
            String effectName = (String) effect.get("effect");
            PotionEffectType potionEffectType = PotionEffectType.getByName(effectName);

            if (potionEffectType != null) {
                int amplifier = (int) effect.get("amplifier");
                int duration = (int) effect.get("duration");
                if (apply) {
                    player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier));
                    debugManager.sendInfo(player.getName() + " received effect: " + effectName +
                            " (amplifier: " + amplifier + ", duration: " + duration + ")");
                } else if (player.getPotionEffect(potionEffectType) != null && player.getPotionEffect(potionEffectType).getDuration() <= duration) {
                    player.removePotionEffect(potionEffectType);
                    debugManager.sendInfo(player.getName() + " lost effect: " + effectName);
                }
            } else {
                debugManager.sendWarning("Invalid config effect type: " + effectName);
            }
        }
    }
}
