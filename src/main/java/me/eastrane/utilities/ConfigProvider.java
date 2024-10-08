package me.eastrane.utilities;

import me.eastrane.EastZombies;
import me.eastrane.utilities.core.BaseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConfigProvider extends BaseConfig {
    private String language;
    private boolean debugConsole, debugFile;
    private boolean broadcastDay, dropHead, changeSkin, dropFlesh, resetRespawnOnFirstDeath, effects;
    private boolean target, targetAtNight, flesh, fleshAtNight, sunBurn, sunBurnAtNight, sunBurnHelmetProtection, hunger, hungerAtNight, golems, golemsAtNight, zombieCompass, zombieCompassAtNight;
    private boolean voiceChatBlockGroupsCreation, voiceChatPersistentGroups, voiceChatJoinOnJoin, voiceChatJoinOnDeath, voiceChatJoinTeamOnly, friendlyFireZombies, friendlyFireHumans;
    private int borderInitialRadius, borderCenterX, borderCenterZ;
    private boolean borderShrinkEnabled;
    private int borderShrinkStartDay, borderShrinkInterval, borderShrinkAmount, borderShrinkDuration, borderShrinkMinRadius;
    private int targetDay, fleshDay, sunBurnDay, hungerDay, golemsDay, zombieCompassDay;
    private int dropFleshAmount, invulnerability, sunBurnDamage, sunBurnHelmetDurabilityLoss, hungerDuration, zombieCompassCooldown;
    private List<String> zombieCompassRecipe;
    private List<Map<?, ?>> effectsList;
    private List<?> restrictedCommandsList;

    public ConfigProvider(EastZombies plugin) {
        super(plugin);
    }

    @Override
    public void loadCustomConfig() {
        language = config.getString("language");
        debugConsole = config.getBoolean("debug.console");
        debugFile = config.getBoolean("debug.file");
        broadcastDay = config.getBoolean("broadcast_day");
        dropHead = config.getBoolean("player.drop_head");
        changeSkin = config.getBoolean("player.change_skin");
        dropFlesh = config.getBoolean("player.flesh.drop_flesh");
        dropFleshAmount = config.getInt("player.flesh.amount");
        resetRespawnOnFirstDeath = config.getBoolean("player.reset_respawn_on_first_death");
        invulnerability = config.getInt("player.invulnerability");
        effects = config.getBoolean("player.effects.enabled");
        effectsList = config.getMapList("player.effects.list");
        restrictedCommandsList = config.getList("player.restricted_commands");

        voiceChatBlockGroupsCreation = config.getBoolean("teams.voicechat.block_groups_creation");
        voiceChatPersistentGroups = config.getBoolean("teams.voicechat.persistent_groups");
        voiceChatJoinOnJoin = config.getBoolean("teams.voicechat.join.on_join");
        voiceChatJoinOnDeath = config.getBoolean("teams.voicechat.join.on_death");
        voiceChatJoinTeamOnly = config.getBoolean("teams.voicechat.join.team_only");

        friendlyFireZombies = config.getBoolean("teams.friendly_fire.zombies");
        friendlyFireHumans = config.getBoolean("teams.friendly_fire.humans");

        borderInitialRadius = config.getInt("world.border.initial_radius");
        borderCenterX = config.getInt("world.border.center_x");
        borderCenterZ = config.getInt("world.border.center_z");
        borderShrinkEnabled = config.getBoolean("world.border.shrink.enabled");
        borderShrinkStartDay = config.getInt("world.border.shrink.start_day");
        borderShrinkInterval = config.getInt("world.border.shrink.interval");
        borderShrinkAmount = config.getInt("world.border.shrink.amount");
        borderShrinkDuration = config.getInt("world.border.shrink.duration");
        borderShrinkMinRadius = config.getInt("world.border.shrink.min_radius");

        target = config.getBoolean("features.target.enabled");
        targetDay = config.getInt("features.target.start_day");
        targetAtNight = config.getBoolean("features.target.at_night");

        flesh = config.getBoolean("features.flesh.enabled");
        fleshDay = config.getInt("features.flesh.start_day");
        fleshAtNight = config.getBoolean("features.flesh.at_night");

        sunBurn = config.getBoolean("features.sun_burn.enabled");
        sunBurnDay = config.getInt("features.sun_burn.start_day");
        sunBurnAtNight = config.getBoolean("features.sun_burn.at_night");
        sunBurnDamage = config.getInt("features.sun_burn.damage");
        sunBurnHelmetProtection = config.getBoolean("features.sun_burn.protection.helmet");
        sunBurnHelmetDurabilityLoss = config.getInt("features.sun_burn.protection.durability_loss");

        hunger = config.getBoolean("features.hunger.enabled");
        hungerDay = config.getInt("features.hunger.start_day");
        hungerAtNight = config.getBoolean("features.hunger.at_night");
        hungerDuration = config.getInt("features.hunger.duration");

        golems = config.getBoolean("features.golems.enabled");
        golemsDay = config.getInt("features.golems.start_day");
        golemsAtNight = config.getBoolean("features.golems.at_night");

        zombieCompass = config.getBoolean("features.zombie_compass.enabled");
        zombieCompassDay = config.getInt("features.zombie_compass.start_day");
        zombieCompassAtNight = config.getBoolean("features.zombie_compass.at_night");
        zombieCompassCooldown = config.getInt("features.zombie_compass.cooldown");
        zombieCompassRecipe = new ArrayList<>();
        zombieCompassRecipe.add( config.getString("features.zombie_compass.recipe.first_row"));
        zombieCompassRecipe.add(config.getString("features.zombie_compass.recipe.second_row"));
        zombieCompassRecipe.add(config.getString("features.zombie_compass.recipe.third_row"));
    }

    @Override
    protected void reloadCustomConfig() {
        plugin.getFeaturesManager().reactivateEvents();
    }

    public String getLanguage() {
        return language;
    }
    public boolean isDebugConsole() {
        return debugConsole;
    }
    public boolean isDebugFile() {
        return debugFile;
    }
    public boolean isBroadcastDay() {
        return broadcastDay;
    }
    public boolean isDropHead() {
        return dropHead;
    }
    public boolean isChangeSkin() {
        return changeSkin;
    }
    public boolean isDropFlesh() {
        return dropFlesh;
    }
    public int getDropFleshAmount() {
        return dropFleshAmount;
    }
    public boolean isResetRespawnOnFirstDeath() {
        return resetRespawnOnFirstDeath;
    }
    public int getInvulnerability() {
        return invulnerability;
    }
    public boolean isEffects() {
        return effects;
    }
    public List<Map<?, ?>> getEffectsList() {
        return effectsList;
    }
    public List<?> getRestrictedCommandsList() { return restrictedCommandsList; }

    public boolean isVoicePersistentGroups() {
        return voiceChatPersistentGroups;
    }
    public boolean isVoiceJoinOnJoin() {
        return voiceChatJoinOnJoin;
    }
    public boolean isVoiceJoinOnDeath() {
        return voiceChatJoinOnDeath;
    }
    public boolean isVoiceJoinTeamOnly() {
        return voiceChatJoinTeamOnly;
    }
    public boolean isVoiceBlockGroupsCreation() {
        return voiceChatBlockGroupsCreation;
    }

    public boolean isFriendlyFireZombies() {
        return friendlyFireZombies;
    }
    public boolean isFriendlyFireHumans() {
        return friendlyFireHumans;
    }

    public int getBorderInitialRadius() {
        return borderInitialRadius;
    }
    public int getBorderCenterZ() {
        return borderCenterZ;
    }
    public int getBorderCenterX() {
        return borderCenterX;
    }
    public boolean isBorderShrinkEnabled() {
        return borderShrinkEnabled;
    }
    public int getBorderShrinkStartDay() {
        return borderShrinkStartDay;
    }
    public int getBorderShrinkInterval() {
        return borderShrinkInterval;
    }
    public int getBorderShrinkAmount() {
        return borderShrinkAmount;
    }
    public int getBorderShrinkDuration() {
        return borderShrinkDuration;
    }
    public int getBorderShrinkMinRadius() {
        return borderShrinkMinRadius;
    }

    public boolean isTarget() {
        return target;
    }
    public int getTargetDay() {
        return targetDay;
    }
    public boolean isTargetAtNight() {
        return targetAtNight;
    }

    public boolean isFlesh() {
        return flesh;
    }
    public int getFleshDay() {
        return fleshDay;
    }
    public boolean isFleshAtNight() {
        return fleshAtNight;
    }

    public boolean isSunBurn() {
        return sunBurn;
    }
    public int getSunBurnDay() {
        return sunBurnDay;
    }
    public boolean isSunBurnAtNight() {
        return sunBurnAtNight;
    }
    public int getSunBurnDamage() {
        return sunBurnDamage;
    }
    public boolean isSunBurnHelmetProtection() {
        return sunBurnHelmetProtection;
    }
    public int getSunBurnHelmetDurabilityLoss() {
        return sunBurnHelmetDurabilityLoss;
    }

    public boolean isHunger() {
        return hunger;
    }
    public int getHungerDay() {
        return hungerDay;
    }
    public boolean isHungerAtNight() {
        return hungerAtNight;
    }
    public int getHungerDuration() {
        return hungerDuration;
    }

    public boolean isGolems() {
        return golems;
    }
    public int getGolemsDay() {
        return golemsDay;
    }
    public boolean isGolemsAtNight() {
        return golemsAtNight;
    }

    public boolean isZombieCompass() {
        return zombieCompass;
    }
    public int getZombieCompassDay() {
        return zombieCompassDay;
    }
    public boolean isZombieCompassAtNight() {
        return zombieCompassAtNight;
    }
    public int getZombieCompassCooldown() {
        return zombieCompassCooldown;
    }
    public List<String> getZombieCompassRecipe() { return zombieCompassRecipe; }
}
