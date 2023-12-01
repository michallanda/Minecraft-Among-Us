package wtf.drav.amongus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Game {
	private Plugin plugin;
	private World world;
	private Lobby lobby;
	private List<ArmorStand> armorstands;
	
	public Game(Plugin plugin, World world, Lobby lobby) {
		this.plugin = plugin;
		this.world = world;
		armorstands = new ArrayList<ArmorStand>();
		for(Entity entity : world.getEntities()) {
			if(entity.getType().equals(EntityType.ARMOR_STAND)) {
				armorstands.add((ArmorStand)entity);
			}
		}
		this.lobby = lobby;
		tpSpawn();
		standTracker();
	}
	
	public void tpSpawn() {
		for(Player player : lobby.getPlayers()) {
			player.teleport(world.getSpawnLocation());
		}
	}
	
	public void standTracker() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player player : lobby.getPlayers()) {
					boolean nearby = false;
					for(ArmorStand armorstand : armorstands) {
				    	if(isCloseEnough(player.getLocation(), armorstand.getLocation(), 3)) {
				    		nearby = true;
				    		break;
				    	}
					}
					if(nearby)	player.getInventory().setItem(8, new ItemStack(Material.DIAMOND));
					else	player.getInventory().setItem(8, new ItemStack(Material.AIR));
			    }	
			}
		}.runTaskTimer(plugin, 1, 1);
	}
	
	public boolean isCloseEnough(Location playerLoc, Location armorstandLoc, int distance) {
		if(armorstandLoc.distanceSquared(playerLoc) > distance * distance) {
			return false;
		}
		return true;
	}
	
}
