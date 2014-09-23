package com.pwncraftpvp.prisoncells.core;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;

public class Main extends JavaPlugin{
	
	private static Main instance;
	
	/**
	 * Get the instance of this class
	 * @return - The instance of this class
	 */
	public static Main getInstance(){
		return instance;
	}
	
	public void onEnable(){
		instance = this;
		if(this.getConfig().getBoolean("doNotChangeMe") == false){
			this.getConfig().set("doNotChangeMe", true);
			this.saveConfig();
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			PPlayer pplayer = new PPlayer(player);
			if(cmd.getName().equalsIgnoreCase("cell")){
				if(args.length > 0){
					if(args[0].equalsIgnoreCase("home")){
						if(pplayer.hasCell() == false){
							try{
								pplayer.createCell();
								player.teleport(pplayer.getCellLocation());
								pplayer.sendMessage("You have generated a new cell!");
							}catch (MaxChangedBlocksException | DataException | IOException e){
								e.printStackTrace();
							}
						}else{
							player.teleport(pplayer.getCellLocation());
							pplayer.sendMessage("You have teleported to your cell!");
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
