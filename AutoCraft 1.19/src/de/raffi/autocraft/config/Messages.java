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
	@Config("message.searchitem")
	public static  String SEARCH_ITEM = "§7Enter search parameters";
	@Config("message.page")
	public static  String PAGE = "§7Page§e";
	
	@Config("reciperegistry.delay")
	public static int RECIPEREGISTRY_DELAY = 0;
	
	@Config("block.place")
	public static  String BLOCK_PLACED = "§aPlaced %block%";
	@Config("block.remove")
	public static  String BLOCK_REMOVED = "§cRemoved %block%";
	@Config("block.hopperconnected")
	public static  String BLOCK_HOPPER_CONNECTED = "§aConnected hopper";
	@Config("block.hopperdisconnected")
	public static  String BLOCK_HOPPER_DISCONNECTED = "§cDisconnected hopper";
	
	@Config("item.resetsearch.name")
	public static  String ITEM_RESETSEARCH_NAME = "§dReset search";
	@Config("item.search.name")
	public static  String ITEM_SEARCH_NAME = "§dSearch";
	@Config("item.search.lore")
	public static  String ITEM_SEARCH_LORE = "§d%amount% §7results.";
	
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
	@Config("item.autocrafter.lore")
	public static  String ITEM_AUTOCRAFTER_LORE = "§7Automatic §ecrafting §7block";
	@Config("item.autoenchanter.lore")
	public static  String ITEM_AUTOENCHANTER_LORE = "§7Automatic §5enchanting §7block";
	@Config("item.oreblock.lore")
	public static  String ITEM_OREBLOCK_LORE = "§7Converts cobblestone into §ddiamonds";
	@Config("item.blockcrusher.lore")
	public static  String ITEM_BOCKCRUSHER_LORE = "§7Uncraft items";
	
	@Config("iventorytitles.autocrafter.recipe")
	public static  String INVENTORY_TITLE_RECIPES = "§dRecipes";
	@Config("iventorytitles.autocrafter.menue")
	public static  String INVENTORY_TITLE_AUTOCRAFTER_MENUE = "§dSelect";
	@Config("iventorytitles.autocrafter.menue")
	public static  String INVENTORY_TITLE_SELECTOPTION = "§dSelect";
	
	@Config("setting.block.blockconverter.probability")
	public static int DIAMOND_PROBABILITY = 5;
	
	@Config("crafting.allow.autocrafter")
	public static  boolean ALLOW_CRAFT_AUTOCRAFT=true;
	@Config("crafting.allow.autoenchanter")
	public static  boolean ALLOW_CRAFT_AUTOENCHANT=true;
	@Config("crafting.allow.oreanalyzer")
	public static  boolean ALLOW_CRAFT_OREBLOCK=true;
	@Config("crafting.allow.blockcrusher")
	public static  boolean ALLOW_CRAFT_BLOCKCRUSHER=true;
	@Config("autosaving.delay")
	public static  int AUTO_SAVING_DELAY=-1;

	
	
	private static File configFile = new File("plugins/AutoCraft/messages.yml");
	private static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	
	public static void loadMessages()  {
		try {
			for(Field f : Messages.class.getFields()) {
				if(!f.isAnnotationPresent(Config.class)) continue;
				f.setAccessible(true);
				Config annotation = f.getAnnotation(Config.class);
				if(config.isSet(annotation.value())) {
					if(f.getType().equals(String.class))
						f.set(null, config.getString(annotation.value()).replace("&", "§"));
					else if(f.getType().equals(Integer.class) || f.getType().equals(int.class)) 
						f.set(null, config.getInt(annotation.value()));
					else if(f.getType().equals(Boolean.class)|| f.getType().equals(boolean.class)) 
						f.set(null, config.getBoolean(annotation.value()));
				} else {
					if(f.getType().equals(String.class))
						config.set(annotation.value(), String.valueOf(f.get(null)).replace("§", "&"));
					else if(f.getType().equals(Integer.class) || f.getType().equals(int.class))
						config.set(annotation.value(), f.getInt(null));
					else if(f.getType().equals(Boolean.class)|| f.getType().equals(boolean.class)) 
						config.set(annotation.value(), f.getBoolean(null));
				}
				
			}
			config.save(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
	}

}
