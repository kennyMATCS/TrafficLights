package me.kenny.trafficlights.runnable;

import me.kenny.trafficlights.TrafficLights;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TrafficLightAutomator {
    private TrafficLights trafficLights;
    private List<TrafficLight> lights = new ArrayList<>();

    public TrafficLightAutomator(TrafficLights trafficLights) {
        this.trafficLights = trafficLights;

        for (String section : trafficLights.getConfig().getKeys(false)) {
            Location greenLightLocation = trafficLights.getLocationFromSerialization(section + ".greenLightLocation");
            Location redLightLocation = trafficLights.getLocationFromSerialization(section + ".redLightLocation");
            int greenLightTime = trafficLights.getConfig().getInt(section + ".greenLightTime");
            int redLightTime = trafficLights.getConfig().getInt(section + ".redLightTime");

            TrafficLight light = new TrafficLight(LightState.GREEN, greenLightLocation, redLightLocation, greenLightTime, redLightTime);
            lights.add(light);

            Block block = greenLightLocation.getWorld().getBlockAt(greenLightLocation);
            block.setType(light.getState().getType());
            block.setData(light.getState().getData());
        }

        Bukkit.getScheduler().runTaskTimer(trafficLights, new BukkitRunnable() {
            @Override
            public void run() {
                for (TrafficLight light : lights) {
                    if (light.isFinished()) {
                        if (light.getState().getOpposite() == LightState.RED)
                            light.setState(LightState.RED);
                        else
                            light.setState(LightState.GREEN);

                        light.resetStartTime();
                    }
                }
            }
        }, 0L, 20L);
    }
}
