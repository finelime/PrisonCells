package com.pwncraftpvp.prisoncells.core;

import java.io.IOException;
import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.pwncraftpvp.prisoncells.tasks.TeleportTask;
import com.pwncraftpvp.prisoncells.utils.Utils;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;

public class Main extends JavaPlugin{
	
	private static Main instance;
	private String gray = ChatColor.GRAY + "";
	private String yellow = ChatColor.YELLOW + "";
	
	public Economy econ;
	
	public HashMap<String, TeleportTask> teleporting = new HashMap<String, TeleportTask>();
	
	/**
	 * Get the instance of this class
	 * @return - The instance of this class
	 */
	public static Main getInstance(){
		return instance;
	}
	
	public void onEnable(){
		instance = this;
		Utils.setupEconomy(this);
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		if(this.getConfig().getBoolean("doNotChangeMe") == false){
			this.getConfig().set("doNotChangeMe", true);
			this.saveConfig();
		}
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				Bukkit.getWorld("Cells").setTime(6000);
				Bukkit.getWorld("Cells").setWeatherDuration(0);
			}
		}, 100, 100);
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			PPlayer pplayer = new PPlayer(player);
			if(cmd.getName().equalsIgnoreCase("cell")){
				if(args.length > 0){
					if(args[0].equalsIgnoreCase("home")){
						if(args.length == 1){
							if(!teleporting.containsKey(player.getName())){
								if(pplayer.hasCell() == false){
									if(econ.getBalance(player.getName()) >= Utils.getCellPrice()){
										try{
											econ.withdrawPlayer(player.getName(), Utils.getCellPrice());
											pplayer.createCell();
											pplayer.sendMessage("You are teleporting to your cell. Stand still.");
											TeleportTask task = new TeleportTask(player, pplayer.getCellLocation());
											task.runTaskTimer(this, 0, 20);
											teleporting.put(player.getName(), task);
										}catch (MaxChangedBlocksException | DataException | IOException e){
											e.printStackTrace();
										}
									}else{
										pplayer.sendError("You need $" + Utils.getCellPrice() + " to create a new cell!");
									}
								}else{
									pplayer.sendMessage("You are teleporting to your cell. Stand still.");
									TeleportTask task = new TeleportTask(player, pplayer.getCellLocation());
									task.runTaskTimer(this, 0, 20);
									teleporting.put(player.getName(), task);
								}
							}else{
								pplayer.sendError("You are already teleporting!");
							}
						}else if(args.length == 2){
							if(player.isOp()){
								PPlayer cellplayer = new PPlayer(args[1]);
								if(cellplayer.getCellLocation().getY() > 0){
									player.teleport(cellplayer.getCellLocation());
									pplayer.sendMessage("You have teleported to " + yellow + args[1] + gray + "'s cell!");
								}else{
									pplayer.sendError("This player does not have a cell!");
								}
							}else{
								pplayer.sendError("You must be an operator to do this!");
							}
						}else{
							pplayer.sendError("Usage: /cell home <player>");
						}
					}else if(args[0].equalsIgnoreCase("setprice")){
						if(args.length == 2){
							if(Utils.isInteger(args[1]) == true){
								int price = Integer.parseInt(args[1]);
								Utils.setCellPrice(price);
								pplayer.sendMessage("You have set the cell price to $" + yellow + price + gray + "!");
							}else{
								pplayer.sendError("You need to enter a valid price!");
							}
						}else{
							pplayer.sendError("Usage: /cell setprice <price>");
						}
					}else{
						pplayer.sendCommandHelp();
					}
				}else{
					pplayer.sendCommandHelp();
				}
			}
		}
		return false;
	}

}
