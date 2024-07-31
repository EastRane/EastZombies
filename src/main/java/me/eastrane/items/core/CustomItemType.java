package me.eastrane.items.core;

public enum CustomItemType {
    ZOMBIE_COMPASS("zombie_compass");

    private final String identifier;

    CustomItemType(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the unique identifier for this custom item type.
     *
     * @return The unique identifier for this custom item type.
     */
    public String getIdentifier() {
        return identifier;
    }
}