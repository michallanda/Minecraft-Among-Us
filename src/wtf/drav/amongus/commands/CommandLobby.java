package wtf.drav.amongus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wtf.drav.amongus.Lobby;
import wtf.drav.amongus.LobbyManager;
import wtf.drav.amongus.Main;

public class CommandLobby implements CommandExecutor {
    private Main plugin;
	
	public CommandLobby(Main plugin) {
    	this.plugin = plugin;
    }
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player player = (Player)sender;
    	LobbyManager lobbies = plugin.getLobbyManager();
    	if(args[0].equals("new")) {
    		Lobby lobby = new Lobby(plugin);
    		lobbies.addLobby(player,lobby);
    	}
    	else if(args[0].equals("join")) {
    		lobbies.addPlayer(player, args[1]);
    	}
    	else if(args[0].equals("leave")) {
    		lobbies.removePlayer(player);
    	}
    	else if(args[0].equals("list")) {
    		for(Player lobbyPlayer : lobbies.getLobby(player.getUniqueId()).getPlayers()) {
    			player.sendMessage(lobbyPlayer.getName());
    		}
    	}
    	else if(args[0].equals("getowner")) {
    		player.sendMessage(lobbies.getLobby(player.getUniqueId()).getPlayers().get(0).getName());
    	}
    	else if(args[0].equals("getlobbies")) {
    		for(Lobby lobby : lobbies.getLobbies()) {
    			player.sendMessage("Lobby: " + lobby.getLobbyID());
    		}
    	}
    	else if(args[0].equals("startgame")) {
    		lobbies.startGame(player.getUniqueId());
    	}
    	
    	return true;
    }
}
