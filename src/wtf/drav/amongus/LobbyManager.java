package wtf.drav.amongus;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import org.bukkit.entity.Player;

public class LobbyManager {
	
	Hashtable<UUID, Lobby> players;
	Hashtable<String, Lobby> lobbies;
	Main plugin;
	GameManager games;
	
	public LobbyManager(Main plugin) {
		this.plugin = plugin;
		lobbies = new Hashtable<String, Lobby>();
		players = new Hashtable<UUID, Lobby>();
		games =   new GameManager(plugin,"amongusworld");
		games.newWorld(0);
		games.newWorld(1);
		games.newWorld(2);
		games.newWorld(3);
	}
	
	public void addLobby(Player player, Lobby lobby) {
		if(players.get(player.getUniqueId()) == null){
			lobbies.put(lobby.getLobbyID(), lobby);
    		player.sendMessage("Lobby " + lobby.getLobbyID() + " opened!");
    		addPlayer(player, lobby.getLobbyID());
		}
		else {
			player.sendMessage("You are already in a party, and cannot join a new one!");
		}
	}
	
	public void removeLobby(Lobby lobby) {
		lobbies.remove(lobby.getLobbyID());
	}
	
	public Lobby getLobby(String lobbyID) {
		return lobbies.get(lobbyID);
	}
	
	public Lobby getLobby(UUID playerUUID) {
		return players.get(playerUUID);
	}
	
	public ArrayList<Lobby> getLobbies(){
		return new ArrayList<>(lobbies.values());
	}
	
	public void addPlayer(Player player, String lobbyID) {
		Lobby lobby;
		if(lobbies.get(lobbyID) != null)	
		{
			lobby = lobbies.get(lobbyID);
			if(lobby.getPlayers().contains(player)) {
				player.sendMessage("You are already in this party!");
				return;
			}
			else if(players.get(player.getUniqueId()) != null) {
				player.sendMessage("You are already in a different party!");
				return;
			}
			if(lobby.getSize() < 10) {
				lobby.addPlayer(player);
				lobby.sendMessage(player.getName() + " Joined the Lobby!");
				players.put(player.getUniqueId(), lobby);
			}
			else player.sendMessage("Sorry, this lobby is full!");
			return;
		}
		player.sendMessage("This lobby doesnt exist!");
	}
	
	public void removePlayer(Player player) {
		Lobby lobby;
		if(players.get(player.getUniqueId()) != null) {
			lobby = players.remove(player.getUniqueId());
			lobby.removePlayer(player);
			player.sendMessage("You have left the lobby!");
			if(lobby.getSize() == 0) {
				lobbies.remove(lobby.getLobbyID());
			}
			else {
				lobby.sendMessage(player.getName() + " has left the Lobby.");
			}
		}
	}
	
	public void startGame(UUID playerUUID) {
		games.startGame(getLobby(playerUUID));
	}
}
