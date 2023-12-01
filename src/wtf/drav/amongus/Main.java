package wtf.drav.amongus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import wtf.drav.amongus.commands.CommandLobby;
import wtf.drav.amongus.commands.CommandWorldCreator;

public class Main extends JavaPlugin implements Listener {
	
	public Main plugin;
	
	private File customConfigFile;
	 private FileConfiguration config;
	 
	public LobbyManager lobbies;
	
	public LobbyManager getLobbyManager() {
		return lobbies;
	}
	// On Enable
	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		
		lobbies = new LobbyManager(plugin);
		
		customConfigFile = new File(getDataFolder(), "vents.yml");
		
		this.getCommand("testvents").setExecutor(new CommandTestVents());
		this.getCommand("worldtp").setExecutor(new WorldTP());
		
		this.getCommand("lobby").setExecutor(new CommandLobby(plugin));
		this.getCommand("MongusMapCreator").setExecutor(new CommandWorldCreator(plugin));
		
		MapFileCreate("test");
		getLogger().info("DravEntityEater has been enabled!");
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
    String[] worlds = new String[20];
    
    public class WorldTP implements CommandExecutor {
    	@Override
    	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    		World world = Bukkit.getWorld(args[0]);
    		((Player)sender).teleport(world.getSpawnLocation());
    		return true;
    	}
    }
       
    // set save schem
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
    	Bukkit.broadcastMessage(e.getMaterial().name());
    	if(e.getPlayer().getItemInHand().getType().equals(Material.ARMOR_STAND)) {
    		e.setCancelled(true);
    		ArmorStand stand = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(.5,1,.5), EntityType.ARMOR_STAND);
    		stand.setCustomName(e.getItem().getItemMeta().getDisplayName());
    		stand.setCustomNameVisible(true);
    	}
    	else if(e.getPlayer().getItemInHand().getType().equals(Material.DIAMOND)) {
    		e.getPlayer().performCommand("testvents");
    	}
    }
    
    int num = 0;
    @EventHandler
    public void onArmorstand(PlayerInteractAtEntityEvent e) {
    	if(e.getPlayer().getItemInHand().getType().equals(Material.STICK)) {
    		e.setCancelled(true);
    		Entity stand = e.getRightClicked();
    		ItemStack item = e.getPlayer().getItemInHand();
    		if(stand.getCustomName().split(":").length == 1) {
    			stand.setCustomName(stand.getCustomName().concat(":?:?:?:?"));
    		}
    		if(item.getItemMeta().getDisplayName().split(":").length == 3) {
    			String name = "";
    			String[] str = stand.getCustomName().split(":");
    			if(item.getItemMeta().getDisplayName().split(":")[1].equals("LEFT")) {
    				str[2] = item.getItemMeta().getDisplayName().split(":")[2];
    			}
    			if(item.getItemMeta().getDisplayName().split(":")[1].equals("MIDDLE")) {
    				str[3] = item.getItemMeta().getDisplayName().split(":")[2];
    			}
    			if(item.getItemMeta().getDisplayName().split(":")[1].equals("RIGHT")) {
    				str[4] = item.getItemMeta().getDisplayName().split(":")[2];
    			}
    			str[1] = item.getItemMeta().getDisplayName().split(":")[0];
    			for(String string : str) {
    				name += string + ":";
    			}
    			
    			stand.setCustomName(name);
    		}
    		num++; 
    	}
    }
	
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
    	if(e.getPlayer().getItemInHand().getType().equals(Material.STICK)){
    		if(e.getMessage().split(":").length == 3) {
    			// [ StandNum : Direction : Connection Number ]
    			String[] strList = e.getMessage().split(":");
    			
    			ItemMeta meta = e.getPlayer().getItemInHand().getItemMeta();
    			meta.setDisplayName(e.getMessage());
    			e.getPlayer().getItemInHand().setItemMeta(meta);
    			e.setCancelled(true);
    		}
    	}
    }
    
    boolean venting = false;
	Entity ent = null;
	ArmorStand stand = null;
	
	public class CommandTestVents implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        	if (sender instanceof Player && sender.hasPermission("sootfix.test")) {
        		final Player player = (Player)sender;
        		if(!venting) {
        			for(Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
        				if(entity instanceof ArmorStand && entity.getCustomName().split(":")[0].equals("vent")) {
        					stand = (ArmorStand)entity;
        					for(Entity entity2 : player.getWorld().getNearbyEntities(stand.getLocation(), 2, 2, 2)) {
        						if(entity2 instanceof Zombie) {
        							ent = entity2;
        						}
        					}
        					break;
        				}
        			}
            		if(stand != null) {
            			Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            	            public void run() {
            	            	player.setGameMode(GameMode.SPECTATOR);
            	            	player.setSpectatorTarget(ent);
            	            	if(!venting) {
            	            		TextComponent messageAccept = new TextComponent( "<<< LEFT" );
            						messageAccept.setColor(ChatColor.GREEN);
            						messageAccept.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/testvents left"));
            						
            						TextComponent messageDeny = new TextComponent( "RIGHT >>>" );
            						messageDeny.setColor(ChatColor.GREEN);
            						messageDeny.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/testvents right"));
            						
            						TextComponent messageUnVent = new TextComponent( "Hop Out" );
            						messageUnVent.setColor(ChatColor.WHITE);
            						messageUnVent.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/testvents"));
            						
            						TextComponent message = new TextComponent(" ");
            						message.addExtra(messageAccept);
            						message.addExtra("    ");
            						message.addExtra(messageUnVent);
            						message.addExtra("    ");
            						message.addExtra(messageDeny);
            						player.spigot().sendMessage(message);
            	            	}
            	            	venting = true;
            	            	

            	            }
        		        }, 0, 1);
            		}
        		}
        		else {
        			if(args.length > 0) {
        				String coords = "";
        				if(args[0].equals("left")) {
    	            		coords = stand.getCustomName().split(":")[2];
    	            	}
    	            	else if(args[0].equals("middle")) {
    	            		coords = stand.getCustomName().split(":")[3];
    	            	}
    	            	else if(args[0].equals("right")) {
    	            		coords = stand.getCustomName().split(":")[4];
    	            	}
        				for(Entity entity : player.getWorld().getNearbyEntities(new Location(player.getWorld(),Integer.parseInt(coords.split(",")[0]),Integer.parseInt(coords.split(",")[1]),Integer.parseInt(coords.split(",")[2])), 2, 2, 2)) {
            				if(entity instanceof ArmorStand && entity.getCustomName().split(":")[0].equals("vent")) {
            					stand = (ArmorStand)entity;
            					for(Entity entity2 : player.getWorld().getNearbyEntities(stand.getLocation(), 2, 2, 2)) {
            						if(entity2 instanceof Zombie) {
            							ent = entity2;
            						}
            					}
            					break;
            				}
            			}
        			}
        			else {
            			Bukkit.getScheduler().cancelTasks(plugin);
            			player.teleport(new Location(player.getWorld(),player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(),player.getLocation().getYaw(),player.getLocation().getPitch()));
            			player.setGameMode(GameMode.SURVIVAL);
            			venting = false;
            			ent = null;
            			stand = null;
        			}
        		}
        	}
        	return true;
        }
	}
	
    //----------------------------------------------------------------------------------------------------------------------------------
    
	public void MapFileCreate(String mapName) {
		folderCreate("\\Maps\\" + mapName);
		fileCreate(plugin.getDataFolder() + "\\Maps\\" + mapName, "vents");
		fileCreate(plugin.getDataFolder() + "\\Maps\\" + mapName, "cams");
		fileCreate(plugin.getDataFolder() + "\\Maps\\" + mapName, "tasks");
		fileCreate(plugin.getDataFolder() + "\\Maps\\" + mapName, "sabatage");
		
	}
	
	public void folderCreate(String directory) {
		File file = new File(plugin.getDataFolder() + "\\" + directory);
		if(!file.exists()) {
			if(file.mkdirs()) {
				getLogger().info("Folder " + plugin.getDataFolder().toString() + "\\" + directory + " Created!");	
			}
		}
	}
	
	public void fileCreate(String fileDirectory, String fileName) {
		File file = new File(fileDirectory + "\\" + fileName + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// When Disabling
	@Override
	public void onDisable() {
		
		for(String str : worlds) {
			if(str == null) break;
			World world = Bukkit.getWorld(str);
			Bukkit.unloadWorld(world, false);
			if(world.getName().equals("world")) continue;
			File deleteFolder = world.getWorldFolder();
			//WorldManager.deleteWorld(deleteFolder);
			getLogger().info("World: " + world.getName() + " Deleted!");
		}
		getLogger().info("DravEntityEater has been disabled!");
	}
}


