# TrafficLights
1.12.2 Spigot plugin that creates traffic lights that automatically switch.
## Permissions
``` traffic.create ``` Allows usage of all traffic light commands.
## Usage
To begin setting up a traffic light, stack **2 concrete blocks** on top of each other. <br>

Next, run the command `/traffic new` while facing one of the blocks to create the traffic light. <br>

By default, the green light will be set to the top concrete block, but it can be switched to the red light by running the command `/traffic switch` while facing one of the blocks. <br>

Traffic lights can then be toggled by running the command `/traffic toggle` while facing it. <br>

To set the duration a traffic light will stay on red or green, use the commands `/traffic setgreentime <seconds>` and `/traffic setredtime <seconds>` while facing a traffic light. By default, traffic lights will both remain red and green for 10 seconds.

## Commands
`/traffic new` Creates a new traffic light at two currently faced stacked concrete blocks. <br> 

`/traffic switch` Switches the red and green light on the currently faced traffic light. <br>

`/traffic toggle` Toggles the currently faced traffic light. <br>

`/traffic setgreentime <seconds>` Sets the amount of seconds a green light will remain at the currently faced traffic light. <br>

`/traffic setredtime <seconds>` Sets the amount of seconds a red light will remain at the currently faced traffic light. <br>

`/traffic list` Lists all traffic lights.
