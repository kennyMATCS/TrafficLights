package me.kenny.trafficlights.command;

import me.kenny.trafficlights.TrafficLights;
import me.kenny.trafficlights.runnable.TrafficLight;
import org.apache.commons.lang.StringUtils;
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

            Block facing;
            if (args.length != 0) {
                switch (args[0]) {
                    case "new":
                        facing = player.getTargetBlock(null, 10);
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
                    case "delete":
                        facing = player.getTargetBlock(null, 10);
                        if (isTrafficLight(facing)) {
                            TrafficLight light = trafficLights.getTrafficLightAutomator().getLight(facing);
                            trafficLights.deleteTrafficLight(light);
                            light.setBlackConcrete(light.getGreenLightLocation());
                            light.setBlackConcrete(light.getRedLightLocation());
                            player.sendMessage(ChatColor.GREEN + "Deleted traffic light.");
                        } else {
                            player.sendMessage(ChatColor.RED + "You must be facing an existing traffic light!");
                        }
                        break;
                    case "setgreentime":
                        facing = player.getTargetBlock(null, 10);
                        if (isTrafficLight(facing)) {
                            if (args.length >= 2) {
                                if (StringUtils.isNumeric(args[1])) {
                                    TrafficLight trafficLight = trafficLights.getTrafficLightAutomator().getLight(facing);
                                    trafficLight.setGreenLightTime(Integer.valueOf(args[1]));
                                    player.sendMessage(ChatColor.GREEN + "Set the green light time of your currently faced traffic light to " + args[1] + " seconds.");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You must input an amount of seconds!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You must be facing an existing traffic light!");
                        }
                        break;
                    case "setredtime":
                        facing = player.getTargetBlock(null, 10);
                        if (isTrafficLight(facing)) {
                            if (args.length >= 2) {
                                if (StringUtils.isNumeric(args[1])) {
                                    TrafficLight trafficLight = trafficLights.getTrafficLightAutomator().getLight(facing);
                                    trafficLight.setRedLightTime(Integer.valueOf(args[1]));
                                    player.sendMessage(ChatColor.GREEN + "Set the red light time of your currently faced traffic light to " + args[1] + " seconds.");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You must input an amount of seconds!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You must be facing an existing traffic light!");
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

    public boolean isTrafficLight(Block block) {
        if (trafficLights.hasIdenticalTrafficLight(block.getLocation()))
            return true;
        return false;
    }

    public void helpCommand(Player player) {
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Traffic Light Commands");
        player.sendMessage(ChatColor.YELLOW + "/traffic new " + ChatColor.WHITE + "Creates a new traffic light at two currently faced stacked black concrete blocks.");
        player.sendMessage(ChatColor.YELLOW + "/traffic delete " + ChatColor.WHITE + "Deletes the currently faced traffic light.");
        player.sendMessage(ChatColor.YELLOW + "/traffic setgreentime <seconds> " + ChatColor.WHITE + "Sets the amount of seconds a green light will remain at the currently faced traffic light.");
        player.sendMessage(ChatColor.YELLOW + "/traffic setredtime <seconds> " + ChatColor.WHITE + "Sets the amount of seconds a red light will remain at the currently faced traffic light.");
    }
}
