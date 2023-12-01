package wtf.drav.amongus;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.World;

public class GameManager {
	private Queue<World> worldQueue;
	private Hashtable<String, Game> games;
	private String worldType;
	private Main plugin;
	
	public GameManager(Main plugin, String worldType) {
		this.plugin = plugin;
		games = new Hashtable<String, Game>();
		worldQueue = new LinkedList<World>();
		this.worldType = worldType;
		
	}
	public void addWorld(World world) {
		worldQueue.add(world);
	}
	
	public void newWorld() {
		WorldManager.createWorld(this, "Game_" + worldType + (worldQueue.size() + games.size()), worldType, plugin);
	}
	
	public void newWorld(int worldNum) {
		WorldManager.createWorld(this, "Game_" + worldType + worldNum, worldType, plugin);
	}
	
	private World getWorld() {
		if(worldQueue.size() < 10) {
			newWorld();
		}
		return worldQueue.poll();
	}
	
	public void startGame(Lobby lobby) {
		games.put("test", new Game(plugin, getWorld(), lobby));
		lobby.sendMessage("Starting Game!");
	}
}
