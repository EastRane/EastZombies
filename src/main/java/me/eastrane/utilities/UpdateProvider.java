package me.eastrane.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.eastrane.EastZombies;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateProvider extends BukkitRunnable {
    private static final String UPDATE_JSON_URL = "https://eastrane.github.io/json/plugins.json";
    private static String PLUGIN_NAME;
    private final String currentVersion;
    private final EastZombies plugin;
    private final DebugManager debugManager;

    public UpdateProvider(EastZombies plugin) {
        this.plugin = plugin;
        debugManager = plugin.getDebugManager();
        PLUGIN_NAME = plugin.getDescription().getName();
        this.currentVersion = plugin.getDescription().getVersion();
        startUpdateCheck();
    }

    @Override
    public void run() {
        try {
            URL url = new URL(UPDATE_JSON_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                String name = jsonObject.get("name").getAsString();
                String version = jsonObject.get("version").getAsString();
                String downloadUrl = jsonObject.get("download_url").getAsString();
                String message = jsonObject.get("message").getAsString();
                if (name.equalsIgnoreCase(PLUGIN_NAME)) {
                    if (!version.equals(this.currentVersion)) {
                        debugManager.sendWarning("A new update " + version + " is available. Current version: " + currentVersion);
                        debugManager.sendWarning("Download here: " + downloadUrl);
                        if (message.isEmpty()) { return; }
                        debugManager.sendWarning(message);
                    }
                } else {
                    debugManager.sendWarning("Error checking for updates.");
                }
            } else {
                debugManager.sendWarning("Error checking for updates.");
            }
        } catch (Exception e) {
            debugManager.sendException(e);
        }
    }

    public void startUpdateCheck() {
        this.runTaskAsynchronously(this.plugin);
    }
}