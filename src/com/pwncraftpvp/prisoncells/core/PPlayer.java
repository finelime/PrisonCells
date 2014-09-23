package com.pwncraftpvp.prisoncells.core;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.pwncraftpvp.prisoncells.utils.UTFUtils;
import com.pwncraftpvp.prisoncells.utils.Utils;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.data.DataException;

public class PPlayer {
	
	Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String yellow = ChatColor.YELLOW + "";
	
	Player player;
	public PPlayer(Player p){
		player = p;
	}
	
	/**
	 * Get the player's file
	 */
	public File getFile(){
		return new File(main.getDataFolder() + File.separator + "players", player.getUniqueId() + ".yml");
	}
	
	/**
	 * Get the player's config
	 */
	public FileConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(getFile());
	}
	
	/**
	 * Set a value in the player's config
	 * 
	 * @param key - The location of the value to set
	 * @param entry - The value to set
	 */
	public void setConfigValue(String key, Object entry){
		FileConfiguration fc = getConfig();
	    fc.set(key, entry);
	    try{
	      fc.save(getFile());
	    }catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	/**
	 * Send a message header to the player
	 * @param header - The header to be sent
	 */
	public void sendMessageHeader(String header){
		player.sendMessage(gray + "-=(" + yellow + "*" + gray + ")=-" + "  " + yellow + header + "  " + gray + "-=(" + yellow + "*" + gray + ")=-");
	}
	
	/**
	 * Send a message to the player
	 * @param message - The message to be sent
	 */
	public void sendMessage(String message){
		player.sendMessage(ChatColor.GOLD + UTFUtils.getArrow() + gray + " " + message);
	}
	
	/**
	 * Send an error message to the player
	 * @param error - The error message to be sent
	 */
	public void sendError(String error){
		player.sendMessage(ChatColor.GOLD + UTFUtils.getArrow() + ChatColor.DARK_RED + " " + error);
	}
	
	/**
	 * Send the command help page to the player
	 */
	public void sendCommandHelp(){
		this.sendMessageHeader("Command Help");
		this.sendMessage(yellow + "/cell home " + gray + "- Return to your cell!");
	}
	
	/**
	 * Get if the player has a cell
	 * @return - True or false depending on if the player has a cell or not
	 */
	public boolean hasCell(){
		if(this.getConfig().getInt("cell.home.y") > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Set the player's cell home location
	 * @param loc - The location to set the home as
	 */
	public void setCellLocation(Location loc){
		this.setConfigValue("cell.home.x", loc.getX());
		this.setConfigValue("cell.home.y", loc.getY());
		this.setConfigValue("cell.home.z", loc.getZ());
	}
	
	/**
	 * Get the player's cell home location
	 * @return The cell home location
	 */
	public Location getCellLocation(){
		double x,y,z;
		x = this.getConfig().getDouble("cell.home.x");
		y = this.getConfig().getDouble("cell.home.y");
		z = this.getConfig().getDouble("cell.home.z");
		return new Location(Utils.getCellsWorld(), x, y, z);
	}
	
	/**
	 * Generate a new cell for the player
	 * @throws MaxChangedBlocksException
	 * @throws DataException
	 * @throws IOException
	 */
	public void createCell() throws MaxChangedBlocksException, DataException, IOException{
		Location loc = Utils.getNextCellLocation();
		Utils.loadSchematic(Utils.getCellsWorld(), new File(main.getDataFolder(), "cell.schematic"), new Vector(loc.getX(), loc.getY(), loc.getZ()));
		this.setCellLocation(loc);
	}
}
