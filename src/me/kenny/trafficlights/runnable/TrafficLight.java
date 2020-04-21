package me.kenny.trafficlights.runnable;

import me.kenny.trafficlights.TrafficLights;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

public class TrafficLight {
    private long startTime;
    private int greenLightTime;
    private int redLightTime;
    private Location greenLightLocation;
    private Location redLightLocation;
    private LightState state;
    private boolean toggled;
    private TrafficLights trafficLights;

    public TrafficLight(LightState state, Location greenLightLocation, Location redLightLocation, int greenLightTime, int redLightTime, TrafficLights trafficLights) {
        this.trafficLights = trafficLights;
        this.startTime = System.currentTimeMillis();
        this.state = state;
        this.greenLightLocation = greenLightLocation;
        this.redLightLocation = redLightLocation;
        this.greenLightTime = greenLightTime;
        this.redLightTime = redLightTime;
        this.toggled = toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    public Location getGreenLightLocation() {
        return greenLightLocation;
    }

    public Location getRedLightLocation() {
        return redLightLocation;
    }

    public void setGreenLightTime(int s) {
        greenLightTime = s;
        trafficLights.setGreenLightTime(this, s);
        resetStartTime();
    }

    public void setRedLightTime(int s) {
        redLightTime = s;
        trafficLights.setRedLightTime(this, s);
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

    public void setBlackConcrete(Location l) {
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
