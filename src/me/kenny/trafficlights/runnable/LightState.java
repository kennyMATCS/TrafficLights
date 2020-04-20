package me.kenny.trafficlights.runnable;

import org.bukkit.Material;

public enum LightState {
    GREEN(Material.STAINED_CLAY, (byte) 13),
    RED(Material.STAINED_CLAY, (byte) 14);

    private Material type;
    private byte data;

    LightState(Material type, byte data) {
        this.type = type;
        this.data = data;
    }

    public Material getType() {
        return type;
    }

    public byte getData() {
        return data;
    }

    public LightState getOpposite() {
        return equals(GREEN) ? RED : GREEN;
    }
}
