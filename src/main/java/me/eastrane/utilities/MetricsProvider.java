package me.eastrane.utilities;

import me.eastrane.EastZombies;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SimplePie;

import java.util.HashMap;
import java.util.Map;

public class MetricsProvider {
    private ConfigProvider configProvider;
    public MetricsProvider(EastZombies plugin) {
        configProvider = plugin.getConfigProvider();
        int pluginId = 22887;
        Metrics metrics = new Metrics(plugin, pluginId);

        addCustomCharts(metrics);
    }

    private void addCustomCharts(Metrics metrics) {
        metrics.addCustomChart(new DrilldownPie("language", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String language = configProvider.getLanguage();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(language, 1);
            if (language.equals("en_US")) {
                map.put("English", entry);
            } else if (language.equals("ru_RU")) {
                map.put("Russian", entry);
            } else if (language.equals("uk_UA")) {
                map.put("Ukrainian", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));

        metrics.addCustomChart(new SimplePie("target", () -> String.valueOf(configProvider.isTarget())));
        metrics.addCustomChart(new SimplePie("flesh", () -> String.valueOf(configProvider.isFlesh())));
        metrics.addCustomChart(new SimplePie("sunburn", () -> String.valueOf(configProvider.isSunBurn())));
        metrics.addCustomChart(new SimplePie("hunger", () -> String.valueOf(configProvider.isHunger())));
        metrics.addCustomChart(new SimplePie("golems", () -> String.valueOf(configProvider.isGolems())));
        metrics.addCustomChart(new SimplePie("zombie_compass", () -> String.valueOf(configProvider.isZombieCompass())));

    }
}
