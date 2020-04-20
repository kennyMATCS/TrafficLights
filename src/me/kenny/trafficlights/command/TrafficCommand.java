package me.kenny.trafficlights.command;

import me.kenny.trafficlights.TrafficLights;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrafficCommand implements CommandExecutor {
    private TrafficLights trafficLights;

    public TrafficCommand(TrafficLights trafficLights) {
        this.trafficLights = trafficLights;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("traffic.create")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                return true;
            }

            if (args.length != 0) {
                switch (args[0]) {
                    case "new":
                        Block facing = player.getTargetBlock(null, 10);
                        // black concrete is data 15
                        if (isBlackConcrete(facing)) {
                            Block greenLight;
                            Block redLight;
                            Block relativeDown = facing.getRelative(BlockFace.DOWN);
                            Block relativeUp = facing.getRelative(BlockFace.UP);

                            if (isBlackConcrete(relativeDown)) {
                                greenLight = facing;
                                redLight = relativeDown;
                            } else if (isBlackConcrete(relativeUp)) {
                                greenLight = relativeUp;
                                redLight = facing;
                            } else {
                                player.sendMessage(ChatColor.RED + "You must stack 2 black concrete blocks on top of each other to create a traffic light!");
                                break;
                            }

                            if (trafficLights.addTrafficLight(greenLight, redLight)) {
                                player.sendMessage(ChatColor.GREEN + "Created a traffic light at the blocks you are currently facing.");
                            } else {
                                player.sendMessage(ChatColor.RED + "A traffic light with that location already exists!");
                            }

                        } else {
                            player.sendMessage(ChatColor.RED + "You must stack 2 black concrete blocks on top of each other to create a traffic light!");
                        }
                        break;
                    default:
                        helpCommand(player);
                        break;
                }
            } else {
                helpCommand(player);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
        }
        return true;
    }

    public boolean isBlackConcrete(Block block) {
        // black concrete is data 15
        if (block != null && block.getType() == Material.CONCRETE && block.getData() == (byte) 15)
            return true;
        return false;
    }

    public void helpCommand(Player player) {
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Traffic Light Commands");
        player.sendMessage(ChatColor.YELLOW + "/traffic new " + ChatColor.WHITE + "Creates a new traffic light at two currently faced stacked black concrete blocks.");
        player.sendMessage(ChatColor.YELLOW + "/traffic delete " + ChatColor.WHITE + "Deletes the currently faced traffic light.");
        player.sendMessage(ChatColor.YELLOW + "/traffic switch " + ChatColor.WHITE + "Switches the red and green light on the currently faced traffic light.");
        player.sendMessage(ChatColor.YELLOW + "/traffic toggle " + ChatColor.WHITE + "Toggles the currently faced traffic light.");
        player.sendMessage(ChatColor.YELLOW + "/traffic setgreentime <seconds> " + ChatColor.WHITE + "Sets the amount of seconds a green light will remain at the currently faced traffic light.");
        player.sendMessage(ChatColor.YELLOW + "/traffic setredtime <seconds> " + ChatColor.WHITE + "Sets the amount of seconds a red light will remain at the currently faced traffic light.");
        player.sendMessage(ChatColor.YELLOW + "/traffic list " + ChatColor.WHITE + "Lists all traffic lights.");
    }
}
