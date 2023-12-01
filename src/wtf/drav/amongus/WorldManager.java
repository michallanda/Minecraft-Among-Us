package wtf.drav.amongus;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

public class WorldManager {	
	 // SWM Variables
	static SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
	static SlimeLoader fileLoader = slimePlugin.getLoader("file");
	
	// World Creator
	public static boolean createWorld(GameManager manager, String name, String worldType, Main myPlugin) {
		 new BukkitRunnable() {
				public void run() {
					SlimePropertyMap properties = new SlimePropertyMap();
					properties.setString(SlimeProperties.DIFFICULTY, "easy");
					properties.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
					properties.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
					properties.setBoolean(SlimeProperties.PVP, false);
					properties.setInt(SlimeProperties.SPAWN_X, 0);
					properties.setInt(SlimeProperties.SPAWN_Y, 100);
					properties.setInt(SlimeProperties.SPAWN_Z, 0);
					
				    // Note that this method should be called asynchronously
				    SlimeWorld world;
					try {
						world = slimePlugin.loadWorld(fileLoader, worldType, true , properties).clone(name, fileLoader);
					    // This method must be called synchronously
					    
						Bukkit.getScheduler().runTask(myPlugin, new Runnable() {
							public void run() {
								slimePlugin.generateWorld(world);
								myPlugin.getLogger().info("World " + name + " has been successfully created!");
								manager.addWorld(Bukkit.getWorld(name));
							}
						});
					} 
					catch (IOException | WorldAlreadyExistsException | UnknownWorldException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
						e.printStackTrace();
					}
				}
		 }.runTask(myPlugin);
		 return true;
	}
	
	// World Deleter
	public static boolean deleteWorld(String name, Main myPlugin) {
		try {
			Bukkit.unloadWorld(name, false);
			fileLoader.deleteWorld(name);
			myPlugin.getLogger().info("World " + name + " has been successfully deleted!");
		} catch (UnknownWorldException | IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
 

