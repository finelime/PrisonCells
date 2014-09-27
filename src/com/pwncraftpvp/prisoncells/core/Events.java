package com.pwncraftpvp.prisoncells.core;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Events implements Listener{
	
	Main main = Main.getInstance();
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		PPlayer pplayer = new PPlayer(player);
		if(player.getWorld().getName().equalsIgnoreCase("Cells")){
			if(player.getItemInHand().getType() == Material.BOAT){
				event.setCancelled(true);
				pplayer.sendError("You may not place boats in your cell!");
			}
		}
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		PPlayer pplayer = new PPlayer(player);
		if(main.teleporting.containsKey(player.getName())){
			if(event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockZ() != event.getFrom().getBlockZ()){
				main.teleporting.get(player.getName()).cancel();
				main.teleporting.remove(player.getName());
				pplayer.sendError("You moved while teleporting!");
			}
		}
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		PPlayer pplayer = new PPlayer(player);
		if(player.getWorld().getName().equalsIgnoreCase("Cells")){
			if(event.getBlock() != null){
				if(pplayer.getCellLocation().distance(event.getBlock().getLocation()) > 12){
					event.setCancelled(true);
					pplayer.sendError("You may not build here!");
				}else if(pplayer.getCellLocation().distance(event.getBlock().getLocation()) < 2){
					event.setCancelled(true);
					pplayer.sendError("You may not build here!");
				}
			}
		}
	}

}
