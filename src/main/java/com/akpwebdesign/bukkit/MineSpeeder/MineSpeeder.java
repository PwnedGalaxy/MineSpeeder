package com.akpwebdesign.bukkit.MineSpeeder;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MineSpeeder extends JavaPlugin implements Listener, CommandExecutor {
	// This is a logger object, for easy access.
	public static final Logger log = Logger.getLogger("Minecraft");
	
	private double defaultSpeed = 0.4D;
	
	@Override
	public void onDisable() {
		log.info(String.format("[%s] Disabled v%s", getDescription().getName(),
				getDescription().getVersion()));
	}

	@Override
	public void onEnable() {
		
		//save the default config file on first run.
		this.saveDefaultConfig();
		
		log.info(String.format("[%s] Enabled v%s", getDescription().getName(),
				getDescription().getVersion()));
		
		//register our event listeners
		getServer().getPluginManager().registerEvents(this, this);
		
		//register our commands
		getCommand("minespeeder").setExecutor(this);
		
	}
	
	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent evt)
	{		
		//if the vehicle is a minecart...
		if(evt.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart) evt.getVehicle();
			
			//if we made it here, we need to set a max speed.
			cart.setMaxSpeed(this.defaultSpeed * this.getConfig().getDouble("minecart-max-speed-multiplier"));
		}
	}
	
	@EventHandler
	public void onVehicleCreate(VehicleCreateEvent evt)
	{		
		//if the vehicle is a minecart...
		if(evt.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart) evt.getVehicle();
			
			//if we made it here, we need to set a max speed.
			cart.setMaxSpeed(this.defaultSpeed * this.getConfig().getDouble("minecart-max-speed-multiplier"));

		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//if we actually get our command...
		if(command.getName().equalsIgnoreCase("minespeeder"))
		{					
			//if no args, return
			if(args.length <= 0) { return false; }

			//if command is set...
			if("set".startsWith(args[0].toLowerCase())) {
				
				//ensure player has permission...
				if(sender.hasPermission("minespeeder.command.speed.set")) {
				
					//if no args[1], return.
					if(args[1] == null) { return false; }
					
					//number to be used later
					double number = 0;
					
					//try to parse argument into double
					try {
						number = Double.parseDouble(args[1]);
					} catch (NumberFormatException e) {
						//if no parse, send error, return.
						sender.sendMessage(ChatColor.RED + "[MineSpeeder] Invalid number!");
						return false;
					}
					
					//if we make it here, we have a proper value for the config.
					this.getConfig().set("minecart-max-speed-multiplier", number);
					
					//save the new config
					this.saveConfig();
					
					return true;
				}
			}
			
			//if command is view
			if("view".startsWith(args[0].toLowerCase())) {
				
				//if player has permission
				if(sender.hasPermission("minespeeder.command.speed.view")) {
					
					//send them a message with current multiplier from config.
					sender.sendMessage(ChatColor.BLUE + "[MineSpeeder] The speed multiplier is currently set to " 
									   + this.getConfig().getDouble("minecart-max-speed-multiplier") + ".");
					return true;
				}
			}
		}
		
		return true;
	}
}
