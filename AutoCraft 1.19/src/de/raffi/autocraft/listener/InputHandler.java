package de.raffi.autocraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import de.raffi.autocraft.utils.ChatCallbackRegistry;

public class InputHandler implements Listener{
	
	@EventHandler
	public void handleMessage(PlayerChatEvent e) {
		if(ChatCallbackRegistry.getRequest(e.getPlayer())==null) return;
		ChatCallbackRegistry.getRequest(e.getPlayer()).chatMessageReceived(e.getMessage());
		ChatCallbackRegistry.deleteRequest(e.getPlayer());
		e.setCancelled(true);
	}
}
