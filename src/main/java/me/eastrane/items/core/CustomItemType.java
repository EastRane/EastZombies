package me.eastrane.items.core;

public enum CustomItemType {
    ZOMBIE_COMPASS("zombie_compass");

    private final String identifier;

    CustomItemType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}