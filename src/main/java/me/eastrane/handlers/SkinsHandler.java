package me.eastrane.handlers;

import me.eastrane.EastZombies;
import me.eastrane.handlers.core.BaseHandler;
import me.eastrane.utilities.DebugProvider;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.property.SkinProperty;
import net.skinsrestorer.api.storage.PlayerStorage;
import net.skinsrestorer.api.storage.SkinStorage;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class SkinsHandler extends BaseHandler {
    private final EastZombies plugin;
    private final DebugProvider debugProvider;
    public SkinsHandler(EastZombies plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        this.plugin = plugin;
        debugProvider = plugin.getDebugProvider();
        this.skinsRestorerAPI = SkinsRestorerProvider.get();
        this.skinStorage = skinsRestorerAPI.getSkinStorage();
    }

    @Override
    protected boolean shouldRegister(long[] worldTime) {
        return true;
    }

    // zombie skin
    private final String VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTU5MDg0OTkyOTMxOSwKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUwNjE3YzVlZjU1MmQxMDY4Y2RiMDU4ODQ4NTRjMDViMjQxMTI5YjdlYjEyOTk2NDA3YWRlYWM4MDkzYWNhYmUiCiAgICB9CiAgfQp9";
    private final String SIGNATURE = "iPX+xFe2MzSVUrctPM9pxRnB3aHgYqLUyrjR0U6QCmugxqmelk2YsaP7ZR5jtj8AwB6j35nyzIU7RVlX/AjTPF33UBQ4a2Zs2rrKW9t2+4B8TIlHwh5YClh98usqOdbbNB//FLEJAEw2vHnI3k+CqmdpzhBUGBTgqY4LXUYNYguEw8MJJo+d3PSQi8F/X2K6gDq1SDRRV8xKYoziJYhnAN5jePgtmpAEudDamcb8UwoFNe48EokQ1QC6GPaVp3X1SNNsIIdyfPcN9ETdx9qETe1wtfqwPqDyhS5xVNXHHfIfFqWYWAA+w8+qFWVsU/MFICKiyEF5rLYLUxkxvbXSZOKauFt82no3qDK5VsnXJP8LEgP5ByaxZ79V/6TasfXUoBSPj5olJ6NmjfLGEmEho1CqHDaCjZ8LOfQoAgrLHcS3xUcoznrT4YFa5IFYmIzvXxH9OPJC1VyBKo3qYURFL2ZaHKi5eadvQ2QO3+SGX29BSjvcmbiBTOe0dWlQ9h+3W9TD34GhXVviBqrkDraUz4D39D8IS6YYR5frdQMT4NVOJpjvN3tJxAwXs2aNgC9LQd5vn7tgFWQjoMmoKhM5z8z1NAjXfFnMPt64OkrTTNHgfIb8XRtXZ2pniKx0rAdNWyv+WRN9cUKQxEdhOfXS04PYU+I0f72R34/m6dcFTxs=";
    SkinsRestorer skinsRestorerAPI;
    SkinStorage skinStorage;

    public void changeSkin(Player player) throws DataRequestException, MineSkinException {
        skinStorage.setCustomSkinData("zombie", SkinProperty.of(VALUE, SIGNATURE));
        Optional<InputDataResult> result = skinStorage.findOrCreateSkinData("zombie");
        PlayerStorage playerStorage = skinsRestorerAPI.getPlayerStorage();
        playerStorage.setSkinIdOfPlayer(player.getUniqueId(), result.get().getIdentifier());
        skinsRestorerAPI.getSkinApplier(Player.class).applySkin(player);
        debugProvider.sendInfo(player.getName() + " received a zombie skin.");
    }
    public void clearSkin(UUID player) throws DataRequestException {
        Player p = plugin.getServer().getPlayer(player);
        PlayerStorage playerStorage = skinsRestorerAPI.getPlayerStorage();
        playerStorage.removeSkinIdOfPlayer(player);
        skinsRestorerAPI.getSkinApplier(Player.class).applySkin(p);
        debugProvider.sendInfo(p.getName() + "'s skin was cleared.");
    }
}
