package me.eastrane.handlers;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.*;
import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class VoiceHandler extends BaseHandler implements VoicechatPlugin {
    private final ConfigProvider configProvider;
    private final LanguageProvider languageProvider;
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
        configProvider = plugin.getConfigProvider();
        languageProvider = plugin.getLanguageProvider();
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
        if (configProvider.isVoicePersistentGroups()) {
            createGroups();
        }
    }

    private void onPlayerJoin(PlayerConnectedEvent event) {
        if (!configProvider.isVoicePersistentGroups()) return;
        if (!configProvider.isVoiceJoinOnJoin()) return;
        connectToTeamGroup(convertPlayer(event.getConnection().getPlayer()));
    }

    private void onGroupCreate(CreateGroupEvent event) {
        if (event.getConnection() == null) return;
        if (!configProvider.isVoiceBlockGroupsCreation()) return;

        languageProvider.sendMessage(convertPlayer(event.getConnection().getPlayer()), "voicechat.errors.creation_blocked");
        event.cancel();
    }

    private void onGroupJoin(JoinGroupEvent event) {
        if (!configProvider.isVoicePersistentGroups()) return;
        if (!configProvider.isVoiceJoinTeamOnly()) return;

        Player player = convertPlayer(event.getConnection().getPlayer());
        UUID playerTeam = plugin.getBaseStorage().isZombie(player) ? zombieGroupId : humanGroupId;
        if (!event.getGroup().getId().equals(playerTeam)) {
            languageProvider.sendMessage(player, "voicechat.errors.wrong_group");
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
            plugin.getDebugProvider().sendWarning("Voice chat connection for player " + player.getName() + " could not be found.");
            return;
        }
        Group teamGroup = plugin.getBaseStorage().isZombie(player) ? zombieGroup : humanGroup;
        connection.setGroup(teamGroup);
        plugin.getDebugProvider().sendInfo("Player " + player.getName() + " was connected to " + teamGroup.getName() + " voice group.");
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
