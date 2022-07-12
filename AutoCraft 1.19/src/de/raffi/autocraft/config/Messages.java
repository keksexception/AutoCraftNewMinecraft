package de.raffi.autocraft.config;

import java.io.File;
import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {
	
	@Config("messageprefix")
	public static  String PREFIX = "§eAutoCraft §8|";
	
	@Config("message.nopermission")
	public static  String NO_PERMISSION = "§cNo permission";
	
	@Config("message.page")
	public static  String PAGE = "§7Seite§e";
	
	@Config("block.place")
	public static  String BLOCK_PLACED = "§aPlaced %block%";
	@Config("block.remove")
	public static  String BLOCK_REMOVED = "§cRemoved %block%";
	@Config("block.hopperconnected")
	public static  String BLOCK_HOPPER_CONNECTED = "§aConnected hopper";
	@Config("block.hopperdisconnected")
	public static  String BLOCK_HOPPER_DISCONNECTED = "§cDisconnected hopper";
	
	@Config("item.recipes.name")
	public static  String ITEM_RECIPES_NAME = "§aRecipes";
	@Config("item.recipes.lore")
	public static  String ITEM_RECIPES_LORE = "§7Select crafting recipe";
	@Config("item.interninventory.name")
	public static  String ITEM_INTERNINV_NAME = "§cIntern inventory";
	@Config("item.interninventory.lore")
	public static  String ITEM_INTERNINV_LORE = "§7Show inventory";
	@Config("item.craftinginventory.name")
	public static  String ITEM_OVERFLOW_NAME = "§cOverflow inventory";
	@Config("item.craftinginventory.lore")
	public static  String ITEM_OVERFLOW_LORE = "§7Show inventory for crafted items";
	
	@Config("iventorytitles.autocrafter.recipe")
	public static  String INVENTORY_TITLE_RECIPES = "§dRecipes";
	@Config("iventorytitles.autocrafter.menue")
	public static  String INVENTORY_TITLE_AUTOCRAFTER_MENUE = "§dSelect";
	
	
	
	private static File configFile = new File("plugins/AutoCraft/messages.yml");
	private static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	
	public static void loadMessages()  {
		try {
			for(Field f : Messages.class.getFields()) {
				if(!f.isAnnotationPresent(Config.class)) continue;
				f.setAccessible(true);
				Config annotation = f.getAnnotation(Config.class);
				if(config.isSet(annotation.value())) {
					f.set(null, config.getString(annotation.value()).replace("&", "§"));
				} else
					config.set(annotation.value(), String.valueOf(f.get(null)).replace("§", "&"));
			}
			config.save(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
	}

}
