package me.kenny.trafficlights;

import me.kenny.trafficlights.command.TrafficCommand;
import me.kenny.trafficlights.listener.TrafficLightListener;
import me.kenny.trafficlights.runnable.TrafficLight;
import me.kenny.trafficlights.runnable.TrafficLightAutomator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TrafficLights extends JavaPlugin {
    private final int redLightTime = 3;
    private final int greenLightTime = 3;

    private TrafficLightAutomator trafficLightAutomator;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("traffic").setExecutor(new TrafficCommand(this));
        getServer().getPluginManager().registerEvents(new TrafficLightListener(this), this);
        trafficLightAutomator = new TrafficLightAutomator(this);
    }

    public boolean addTrafficLight(Block greenLight, Block redLight) {
        int nextKey = getFirstAvailableKey("");

        if (hasIdenticalTrafficLight(greenLight.getLocation(), "") || hasIdenticalTrafficLight(redLight.getLocation(), ""))
            return false;

        getConfig().set(Integer.valueOf(nextKey).toString() + ".greenLightLocation", getLocationSerialization(greenLight.getLocation()));
        getConfig().set(Integer.valueOf(nextKey).toString() + ".redLightLocation", getLocationSerialization(redLight.getLocation()));
        getConfig().set(Integer.valueOf(nextKey).toString() + ".greenLightTime", greenLightTime);
        getConfig().set(Integer.valueOf(nextKey).toString() + ".redLightTime", redLightTime);

        saveConfig();
        reorganize("");
        reloadConfig();

        return true;
    }

    public Location getLocationFromSerialization(String path) {
        int x = getConfig().getInt(path + ".x");
        int y = getConfig().getInt(path + ".y");
        int z = getConfig().getInt(path + ".z");
        String world = getConfig().getString(path + ".world");
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    private Map<String, Object> getLocationSerialization(Location location) {
        Map<String, Object> map = new HashMap<>();
        map.put("x", location.getBlockX());
        map.put("y", location.getBlockY());
        map.put("z", location.getBlockZ());
        map.put("world", location.getWorld().getName());
        return map;
    }

    public boolean hasIdenticalTrafficLight(Location location, String path) {
        boolean identical = false;
        for (String section : getConfig().getKeys(false)) {
            for (String value : getConfig().getConfigurationSection(section).getKeys(false)) {
                if (value.equals("greenLightLocation") || value.equals("redLightLocation")) {
                    if (isIdenticalLocation(getConfig().getConfigurationSection(section + "." + value).getValues(false), location))
                        identical = true;
                }
            }
        }
        return identical;
    }

    public boolean hasIdenticalTrafficLight(Location location) {
        return hasIdenticalTrafficLight(location, "");
    }

    private boolean isIdenticalLocation(Map<String, Object> values, Location location) {
        // TODO: traffic light setgreentime and setredtime
        // TODO: traffic light list

        int x = 0;
        int y = 0;
        int z = 0;
        String world = "";

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            switch (entry.getKey()) {
                case "x":
                    x = (int) entry.getValue();
                    break;
                case "y":
                    y = (int) entry.getValue();
                    break;
                case "z":
                    z = (int) entry.getValue();
                    break;
                case "world":
                    world = (String) entry.getValue();
                    break;
            }
        }

        return (x == location.getBlockX() && y == location.getBlockY() && z == location.getBlockZ() && world.equals(location.getWorld().getName()));
    }

    // gets the first available key in the config
    private Integer getFirstAvailableKey(String path) {
        int lastKey = 0;

        if (getConfig().getConfigurationSection(path) == null || getConfig().getConfigurationSection(path).getValues(false).isEmpty())
            return 1;

        Set<String> paths = path.equals("") ? getConfig().getKeys(false) : getConfig().getConfigurationSection(path).getKeys(false);

        int lastValue = Integer.parseInt((String) paths.toArray()[paths.size() - 1]);
        for (int i = 1; i < lastValue + 1; i++) {
            if (!getConfig().contains(path + "." + String.valueOf(i))) {
                return i;
            }
            lastKey = i;
        }
        return lastKey + 1;
    }

    // reorganizes the config from least to greatest.
    private void reorganize(String path) {
        Map<Integer, Map<String, Object>> sections = new HashMap<>();

        for (String key : getConfig().getConfigurationSection(path).getKeys(false)) {
            sections.put(Integer.parseInt(key), getConfig().getConfigurationSection(path + "." + key).getValues(true));
        }

        sections = sections.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Map<String, Object>>comparingByKey())
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        clearConfig(path);

        for (Map.Entry<Integer, Map<String, Object>> entry : sections.entrySet()) {
            for (Map.Entry<String, Object> value : entry.getValue().entrySet()) {
                getConfig().set(path + "." + entry.getKey() + "." + value.getKey(), value.getValue());
            }
        }

        saveConfig();
    }

    private void clearConfig(String path) {
        for (String key : getConfig().getConfigurationSection(path).getKeys(false)) {
            getConfig().set(key, null);
        }
    }
}
