package wtf.drav.amongus.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import wtf.drav.amongus.Main;

public class CommandWorldCreator implements CommandExecutor {
    Main plugin;
	
	public CommandWorldCreator(Main plugin) {
    	this.plugin = plugin;
    }
	
    
    private Location corner1;
    private Location corner2;
    private Player playerCreating;
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	// /MongusMapCreator [help | start | gui | cancel | finish {name} | corners | center]  
    	// creates a file for a Mongus Map under entered name
    	// change perms, 
    	
    	if(args.length == 0) { // no arguments 
    		sender.sendMessage("if you need help use: ");
    		return true;
    	}
    	if(!(sender instanceof Player)) { // not a player
    		sender.sendMessage("Please use this in game!");
    		return true;
    	}
    	if(!sender.hasPermission("mongus.saveworld")) { // no permissions
    		sender.sendMessage("You need permissions to create a map!");
    		return true;
    	}
    	
    	// arg check
    	if(args[0].equals("help")) {
    		//send messages
    		sender.sendMessage("You need permissions to create a map!");
    		sender.sendMessage("You need permissions to create a map!");
    		sender.sendMessage("You need permissions to create a map!");
    	}
    	else if(args[0].equals("cancel")) {
    		if(true) { // map creator on
    			//turn off map creator
    		}
    		else { // map creator off
    			sender.sendMessage("You arent currently creating a new map!");
    		}
    	}
    	else if(args[0].equals("gui")) {
    		//open gui
    	}
    	else if(args[0].equals("start")) {
    		playerCreating = (Player)sender;
    		// start a 
    	}
    	else if(args[0].equals("end")) {
    		if(corner1 == null || corner2 == null) {
    			sender.sendMessage("A corner hasn't been set! Use /MungusMapCreator [corner1,corner2] to set the corners!");
    		}
    		else {
    			Map<String, ArrayList<Entity>> stands = new HashMap<String, ArrayList<Entity>>();
    			Chunk chunk1 = corner1.getChunk();
    			Chunk chunk2 = corner2.getChunk();
    			int xMin = Math.min(chunk1.getX(), chunk2.getX());
    			int xMax = Math.max(chunk1.getX(), chunk2.getX());
    			int zMin = Math.min(chunk1.getZ(), chunk2.getZ());
    			int zMax = Math.max(chunk1.getZ(), chunk2.getZ());

    			for(int x = xMin; x <= xMax; x++){
    				for(int z = zMin; z <= zMax; z++){
						for(Entity entity : corner1.getWorld().getChunkAt(x, z).getEntities()) {
							if(entity instanceof ArmorStand && entity.isCustomNameVisible()) {
								if(stands.get(entity.getCustomName().split(":")[0]) == null) {
									stands.put(entity.getCustomName().split(":")[0], new ArrayList<Entity>());
								}
								stands.get(entity.getCustomName().split(":")[0]).add(entity);
							}
						}
					}
				}
    			
    			for(Entity stand : stands.get("vent")){
    				for(Entity stand2 : stands.get("vent")) {
    					String[] strList = stand2.getCustomName().split(":");
    					for(int i = 2; i < strList.length; i++) {
    						if(stand.equals(stand2))	break;
    						if(stand.getCustomName().split(":")[1].equals(strList[i])) {
    							strList[i] = stand.getLocation().getBlockX() + "," + stand.getLocation().getBlockY() + "," + stand.getLocation().getBlockZ();
    							Bukkit.broadcastMessage("test");
    						}
    					}
    					String name = "";
    					for(String string : strList) {
    	    				name += string + ":";
    	    			}
    					Bukkit.broadcastMessage(name);
    					stand2.setCustomName(name);
    				}
    			}
    			
    			for(Map.Entry<String, ArrayList<Entity>> entry : stands.entrySet()) {
    				Bukkit.broadcastMessage(entry.getKey() + " : " + entry.getValue().size());
    			}
    			
    			
    		}
    	}
    	else if(args[0].equals("corner1")) {
    		corner1 = ((Player)sender).getLocation();
    		sender.sendMessage("The first corner has been set!");
    	}
    	else if(args[0].equals("corner2")) {
    		corner2 = ((Player)sender).getLocation();
    		sender.sendMessage("The second corner has been set!");
    	}
    	else if(args[0].equals("setcam")) {
    		Zombie z = (Zombie)((Player)sender).getWorld().spawnEntity(((Player)sender).getLocation(), EntityType.ZOMBIE);
        	z.setGravity(false);
        	z.setAI(false);
        	z.setPersistent(true);
        	z.setCustomName("test");
    	}
    	
    	
    	
    	
    	
    	
        return true;
    }
}