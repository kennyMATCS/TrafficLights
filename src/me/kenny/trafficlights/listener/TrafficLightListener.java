package me.kenny.trafficlights.listener;

import me.kenny.trafficlights.TrafficLights;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TrafficLightListener implements Listener {
    private TrafficLights trafficLights;

    public TrafficLightListener(TrafficLights trafficLights) {
        this.trafficLights = trafficLights;
    }

    @EventHandler
    public void onTrafficLightBreak(BlockBreakEvent event) {
        if (trafficLights.hasIdenticalTrafficLight(event.getBlock().getLocation())) {
            if (event.getPlayer().hasPermission("traffic.create"))
                event.getPlayer().sendMessage(ChatColor.RED + "You can not destroy traffic lights! Remove a traffic light by running the command '/traffic delete`.");
            event.setCancelled(true);
        }
    }
}
