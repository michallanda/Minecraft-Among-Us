package wtf.drav.amongus;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Lobby {
	// Variables
	private String lobbyID;
	private ArrayList<Player> players;
	private int timer;
	private int timerID;
	private Main plugin;
	//settings list
	
	// Constructor
	public Lobby(Main plugin) {
		this.lobbyID = String.valueOf((int) ((Math.random() * (200))));
		this.timer = 0;
		this.timerID = -1;
		players = new ArrayList<Player>();
		this.plugin = plugin;
	}
	
	public void lobbyTimer(boolean active) {
		if(active && timerID == -1) {
			timerID = new BukkitRunnable() {
				public void run() {
					timer++;
					if(timer == 2) {
						closeLobby();
						
					}
				}
			}.runTaskTimer(plugin, 100, 1200).getTaskId();
		}
		else if(!active)
		{
			Bukkit.getScheduler().cancelTask(timerID);
			timerID = -1;
		}
	}
	
	public void closeLobby() {
		Bukkit.getScheduler().cancelTask(timerID);
		timerID = -1;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	public String getLobbyID() {
		return lobbyID;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public int getSize() {
		return players.size();
	}
	
	public void sendMessage(String message) {
		for(Player player : players) {
			player.sendMessage(message);
		}
	}
}
