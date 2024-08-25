package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.DebugProvider;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public class EffectsHandler extends BaseHandler {
    private final EastZombies plugin;
    private DebugProvider debugProvider;

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
        debugProvider = plugin.getDebugProvider();
        List<Map<?, ?>> effects = plugin.getConfigProvider().getEffectsList();
        for (Map<?, ?> effect : effects) {
            String effectName = (String) effect.get("effect");
            PotionEffectType potionEffectType = PotionEffectType.getByName(effectName);

            if (potionEffectType != null) {
                int amplifier = (int) effect.get("amplifier");
                int duration = (int) effect.get("duration");
                PotionEffect currentEffect = player.getPotionEffect(potionEffectType);
                if (apply) {
                    if (currentEffect != null) {
                        continue;
                    }
                    player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier));
                    debugProvider.sendInfo(player.getName() + " received effect: " + effectName +
                            " (amplifier: " + amplifier + ", duration: " + duration + ")");
                } else if (player.getPotionEffect(potionEffectType) != null && player.getPotionEffect(potionEffectType).getDuration() <= duration) {
                    player.removePotionEffect(potionEffectType);
                    debugProvider.sendInfo(player.getName() + " lost effect: " + effectName);
                }
            } else {
                debugProvider.sendWarning("Invalid config effect type: " + effectName);
            }
        }
    }
}
