package me.eastrane.listeners.core;

import me.eastrane.EastZombies;
import me.eastrane.listeners.*;

import java.util.*;

public class ListenerManager {
    private final EastZombies plugin;
    private final Map<String, BaseListener> listeners = new LinkedHashMap<>();

    public ListenerManager(EastZombies plugin) {
        this.plugin = plugin;
        registerListeners();
    }

    private void registerListeners() {
        registerListener(new PlayerDeathListener(plugin, true));
        registerListener(new JoinQuitListener(plugin, true));
        registerListener(new RestrictedCommandsListener(plugin, true));
        registerListener(new EntityTargetListener(plugin, true));
        registerListener(new ItemConsumeListener(plugin, true));
        registerListener(new EntityDamageByEntityListener(plugin, true));
        registerListener(new PlayerInteractListener(plugin, true));
    }

    private void registerListener(BaseListener listener) {
        listeners.put(listener.getClass().getSimpleName(), listener);
    }

    public Map<String, Boolean> recheckListeners(long[] worldTime) {
        Map<String, Boolean> changes = new HashMap<>();

        for (BaseListener listener : listeners.values()) {
            String listenerName = listener.getClass().getSimpleName();
            if (listener.isRegistered()) {
                if (!listener.shouldRegister(worldTime)) {
                    if (listener.unregister()) {
                        changes.put(listenerName, false);
                    }
                }
            } else {
                if (listener.shouldRegister(worldTime)) {
                    if (listener.register()) {
                        changes.put(listenerName, true);
                    }
                }
            }
        }
        return changes;
    }

    public void unregisterListeners() {
        for (BaseListener listener : listeners.values()) {
            listener.unregister();
        }
    }

    public List<String> getRegisteredListeners() {
        List<String> registeredListeners = new ArrayList<>();
        for (BaseListener listener : listeners.values()) {
            if (listener.isRegistered()) {
                registeredListeners.add(listener.getClass().getSimpleName());
            }
        }
        return registeredListeners;
    }

    public BaseListener getListener(String listenerName) {
        return listeners.get(listenerName);
    }
}
