package de.raffi.autocraft.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import de.raffi.autocraft.callback.CallBackChat;

public class ChatCallbackRegistry {
	
	private static HashMap<Player, CallBackChat> requests = new HashMap<>();
	
	
	public static void registerRequest(Player p, CallBackChat callback) throws InvocationTargetException {
		requests.put(p, callback);
	}
	public static CallBackChat getRequest(Player p) {
		return requests.get(p);
	}
	public static CallBackChat deleteRequest(Player p) {
		return requests.remove(p);
	}
}
