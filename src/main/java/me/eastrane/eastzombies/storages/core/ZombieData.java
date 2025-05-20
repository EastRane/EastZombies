package me.eastrane.eastzombies.storages.core;

public class ZombieData {
    private String zombieType;

    public ZombieData(String zombieType) {
        this.zombieType = zombieType;
    }

    public String getZombieType() {
        return zombieType;
    }

    public void setZombieType(String zombieType) {
        this.zombieType = zombieType;
    }
}
