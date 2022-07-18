package de.raffi.autocraft.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.raffi.autocraft.blocks.BlockAutoCrafter;
import de.raffi.autocraft.builder.ItemBuilder;
import de.raffi.autocraft.config.Messages;
import de.raffi.autocraft.recipes.Recipe;
import de.raffi.autocraft.recipes.RecipeRegistry;

public class InventoryTitles {

	
	private static HashMap<Player, Integer> page = new HashMap<>();
	
	public static Inventory getRecipes(Player p, int page) {
		
		Inventory recipes = Bukkit.createInventory(null, 9*4, Messages.INVENTORY_TITLE_RECIPES);
		BlockAutoCrafter crafter = (BlockAutoCrafter) PlayerInteractionStorage.getCurrentBlock(p);
		for(int i = page*9*3; i < page*9*3+9*3; i++) {
			if(i >= RecipeRegistry.getRecipes().size()) break;
			Recipe r = RecipeRegistry.getRecipes().get(i);
			String[] lore = new String[r.getIngrediants().length];
			for(int j = 0; j < lore.length; j++) {
				Material type = r.getIngrediants()[j].getType();
					lore[j] = "§7" + r.getIngrediants()[j].getAmount() + "x " + type.name().toLowerCase().replace("_", " ");
			}
			recipes.setItem(i-page*9*3,new ItemBuilder(r.getTarget().clone())
					.glow(crafter.getTarget().getTarget().getType()==r.getTarget().getType()&&crafter.getTarget().getTarget().equals(r.getTarget()))
					.setLore(lore)
					.build());
		}
		
		recipes.setItem(9*4-3, new ItemBuilder(Material.LEGACY_SIGN).setName("§dSearch").setLore("§7Comming soon").build());
		
		if(page!=0)
			recipes.setItem(9*3, new ItemBuilder(Material.PAPER).setName("§e<<").setLore(Messages.PAGE+" " +page).build());
		
		recipes.setItem(9*4-5, crafter.getTarget().getTarget());
		
		if(RecipeRegistry.getRecipes().size()>(page+1)*9*3)
			recipes.setItem(9*4-1, new ItemBuilder(Material.PAPER).setName("§e>>").setLore(Messages.PAGE+" " + (page+2)).build());
		
		
		return recipes;
	}
	
	public static int getPage(Player p) {
		return page.get(p)==null?0:page.get(p);
	}
	public static void setPage(Player p, int page) {
		InventoryTitles.page.put(p, page);
	}

}
