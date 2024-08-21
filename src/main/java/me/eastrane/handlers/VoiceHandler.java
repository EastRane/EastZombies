package me.eastrane.handlers;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.*;
import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class VoiceHandler extends BaseHandler implements VoicechatPlugin {
    private final ConfigManager configManager;
    private final LanguageManager languageManager;
    private VoicechatServerApi api;
    private Group zombieGroup;
    private Group humanGroup;
    private final UUID zombieGroupId = UUID.nameUUIDFromBytes("Zombies".getBytes(StandardCharsets.UTF_8));
    private final UUID humanGroupId = UUID.nameUUIDFromBytes("Humans".getBytes(StandardCharsets.UTF_8));

    public VoiceHandler(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        BukkitVoicechatService service = plugin.getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            service.registerPlugin(this);
        }
        configManager = plugin.getConfigManager();
        languageManager = plugin.getLanguageManager();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    @Override
    public String getPluginId() {
        return "EastZombies";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onVoiceEnabled);
        registration.registerEvent(CreateGroupEvent.class, this::onGroupCreate);
        registration.registerEvent(JoinGroupEvent.class, this::onGroupJoin);
        registration.registerEvent(PlayerConnectedEvent.class, this::onPlayerJoin);
    }

    private void onVoiceEnabled(VoicechatServerStartedEvent event) {
        api = event.getVoicechat();
        if (configManager.isVoicePersistentGroups()) {
            createGroups();
        }
    }

    private void onPlayerJoin(PlayerConnectedEvent event) {
        if (!configManager.isVoicePersistentGroups()) return;
        if (!configManager.isVoiceJoinOnJoin()) return;
        connectToTeamGroup(convertPlayer(event.getConnection().getPlayer()));
    }

    private void onGroupCreate(CreateGroupEvent event) {
        if (event.getConnection() == null) return;
        if (!configManager.isVoiceBlockGroupsCreation()) return;

        languageManager.sendMessage(convertPlayer(event.getConnection().getPlayer()), "voicechat.errors.creation_blocked");
        event.cancel();
    }

    private void onGroupJoin(JoinGroupEvent event) {
        if (!configManager.isVoicePersistentGroups()) return;
        if (!configManager.isVoiceJoinTeamOnly()) return;

        Player player = convertPlayer(event.getConnection().getPlayer());
        UUID playerTeam = plugin.getDataManager().isZombiePlayer(player) ? zombieGroupId : humanGroupId;
        if (!event.getGroup().getId().equals(playerTeam)) {
            languageManager.sendMessage(player, "voicechat.errors.wrong_group");
            event.cancel();
        }
    }

    private void createGroups() {
        zombieGroup = createGroup(zombieGroupId, "Zombies");
        humanGroup = createGroup(humanGroupId, "Humans");
    }

    public void connectToTeamGroup(Player player) {
        VoicechatConnection connection = api.getConnectionOf(player.getUniqueId());
        if (connection == null) {
            plugin.getDebugManager().sendWarning("Voice chat connection for player " + player.getName() + " could not be found.");
            return;
        }
        Group teamGroup = plugin.getDataManager().isZombiePlayer(player) ? zombieGroup : humanGroup;
        connection.setGroup(teamGroup);
        plugin.getDebugManager().sendInfo("Player " + player.getName() + " was connected to " + teamGroup.getName() + " voice group.");
    }

    private Player convertPlayer(ServerPlayer player) {
        return plugin.getServer().getPlayer(player.getUuid());
    }

    private Group createGroup(UUID id, String name) {
        return api.groupBuilder()
                .setId(id)
                .setName(name)
                .setPersistent(true)
                .setType(Group.Type.OPEN)
                .build();
    }

}
