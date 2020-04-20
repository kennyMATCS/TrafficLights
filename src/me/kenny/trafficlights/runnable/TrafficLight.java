package me.kenny.trafficlights.runnable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class TrafficLight {
    private long startTime;
    private int greenLightTime;
    private int redLightTime;
    private Location greenLightLocation;
    private Location redLightLocation;
    private LightState state;

    public TrafficLight(LightState state, Location greenLightLocation, Location redLightLocation, int greenLightTime, int redLightTime) {
        this.startTime = System.currentTimeMillis();
        this.state = state;
        this.greenLightLocation = greenLightLocation;
        this.redLightLocation = redLightLocation;
        this.greenLightTime = greenLightTime;
        this.redLightTime = redLightTime;
    }

    public void setGreenLightTime(int s) {
        greenLightTime = s;
        resetStartTime();
    }

    public void setRedLightTime(int s) {
        redLightTime = s;
        resetStartTime();
    }

    public LightState getState() {
        return state;
    }

    public void setState(LightState state) {
        this.state = state;
        if (state == LightState.GREEN) {
            Block block = greenLightLocation.getWorld().getBlockAt(greenLightLocation);
            block.setType(state.getType());
            block.setData(state.getData());
            setBlackConcrete(redLightLocation);
        } else {
            Block block = redLightLocation.getWorld().getBlockAt(redLightLocation);
            block.setType(state.getType());
            block.setData(state.getData());
            setBlackConcrete(greenLightLocation);
        }
    }

    private void setBlackConcrete(Location l) {
        Block block = l.getWorld().getBlockAt(l);
        block.setType(Material.CONCRETE);
        block.setData((byte) 15);
    }

    public void resetStartTime() {
        startTime = System.currentTimeMillis();
    }

    public boolean isFinished() {
        if (state.equals(LightState.GREEN))
            return startTime + (greenLightTime * 1000) <= System.currentTimeMillis();
        else
            return startTime + (redLightTime * 1000) <= System.currentTimeMillis();
    }
}
