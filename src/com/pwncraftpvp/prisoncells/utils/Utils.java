package com.pwncraftpvp.prisoncells.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.pwncraftpvp.prisoncells.core.Main;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;

public class Utils {
	
	static Main main = Main.getInstance();
	
	/**
	 * Load a schematic at the given vector
	 * @param world - The world to paste the schematic in
	 * @param file - The file of the schematic
	 * @param origin - The origin to paste the schematic at
	 * @throws DataException
	 * @throws IOException
	 * @throws MaxChangedBlocksException
	 */
	@SuppressWarnings("deprecation")
	public static void loadSchematic(World world, File file,Vector origin) throws DataException, IOException, MaxChangedBlocksException{
	    EditSession es = new EditSession(new BukkitWorld(world), 999999999);
	    CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
	    cc.paste(es, origin, false);
	}
	
	/**
	 * Get the cells world
	 * @return - The cells world
	 */
	public static World getCellsWorld(){
		return Bukkit.getWorld("Cells");
	}
	
	/**
	 * Get the current x value of the latest cell
	 * @return - The current x value of the latest cell
	 */
	public static int getCurrentX(){
		return main.getConfig().getInt("currentX");
	}
	
	/**
	 * Set the current x value of the latest cell
	 * @param cells - The new x value of the latest cell
	 */
	public static void setCurrentX(int currentX){
		main.getConfig().set("currentX", currentX);
		main.saveConfig();
	}
	
	/**
	 * Get the next cell location for a new cell
	 * @return - The location of the next cell to be generated
	 */
	public static Location getNextCellLocation(){
		double x = getCurrentX() + 35;
		double y = 20;
		double z = 0;
		setCurrentX((int)x);
		return new Location(getCellsWorld(), x, y, z);
	}
	
}
