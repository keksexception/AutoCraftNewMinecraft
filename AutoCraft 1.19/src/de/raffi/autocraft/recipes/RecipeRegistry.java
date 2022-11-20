package de.raffi.autocraft.recipes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import de.raffi.autocraft.builder.ItemBuilder;
import de.raffi.autocraft.config.Messages;
import de.raffi.autocraft.main.AutoCraft;

public class RecipeRegistry {
	
	private static List<Recipe> recipes;
	
	public static void add(Recipe r) {
		if(!isAllowed(r.getTarget().getType())) return;
		recipes.add(r);
	}
	public static File configFile = new File("plugins/AutoCraft/recipes.yml");
	public static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
	private static boolean isAllowed(Material m) {
		if( config.isSet("allow." + m.name())) {
			return config.getBoolean("allow." + m.name());
		} 
		config.set("allow." + m.name(), true);
		return true;
	}
		public static void init() {
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(AutoCraft.getAutoCraft(), ()->{
				try {
					if(!configFile.getParentFile().exists())
						configFile.getParentFile().mkdir();
					if(!configFile.exists())
						configFile.createNewFile();
					config.load(configFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				recipes = new ArrayList<>();
				System.out.println("[AutoCraft] Registering recipes ...");
				
				Bukkit.getServer().recipeIterator().forEachRemaining(rec->{
					if(rec instanceof ShapelessRecipe) {
						ShapelessRecipe shapeless = (ShapelessRecipe) rec;
						if(shapeless.getResult().getAmount()!=0&&!containsAir(shapeless.getIngredientList())) {
							add(new Recipe(shapeless.getResult(), summarize(shapeless.getIngredientList()).toArray(ItemStack[]::new)));
						}
					
							
					} else if(rec instanceof ShapedRecipe) {
						ShapedRecipe shaped = (ShapedRecipe) rec;
						if(shaped.getResult().getAmount()!=0 && !containsAir(shaped.getIngredientMap().values())) {
							add(new Recipe(shaped.getResult(), summarize(shaped.getIngredientMap().values().stream().toList()).toArray(ItemStack[]::new)));
						}

					}
				});
				addIncomatible();
				System.out.println("[AutoCraft] Registering recipes completed");
				try {
					config.save(configFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			},Messages.RECIPEREGISTRY_DELAY);

	}
	public static void addIncomatible() {
		System.out.println("[AutoCraft] Adding custom recipes ...");
		recipes.add(new Recipe(new ItemBuilder(Material.STICK).setAmount(4).build(), new ItemBuilder(Material.SPRUCE_PLANKS).setAmount(2).build()));
		recipes.add(new Recipe(new ItemBuilder(Material.STICK).setAmount(4).build(), new ItemBuilder(Material.BIRCH_PLANKS).setAmount(2).build()));
		recipes.add(new Recipe(new ItemBuilder(Material.STICK).setAmount(4).build(), new ItemBuilder(Material.DARK_OAK_PLANKS).setAmount(2).build()));
		recipes.add(new Recipe(new ItemBuilder(Material.STICK).setAmount(4).build(), new ItemBuilder(Material.ACACIA_PLANKS).setAmount(2).build()));
		recipes.add(new Recipe(new ItemBuilder(Material.STICK).setAmount(4).build(), new ItemBuilder(Material.JUNGLE_PLANKS).setAmount(2).build()));
	}
	private static boolean containsAir(Collection<ItemStack> stack) {
		for(ItemStack s : stack) {
			if(s == null) continue;
			if(s.getType()==Material.AIR) return true;
		}
		return false;
	}
	private static List<ItemStack> summarize(List<ItemStack> list) {
		List<ItemStack>  summarized= new ArrayList<>();
		List<ItemStack>  checkedStacks= new ArrayList<>();
		Stream<ItemStack> s = list.stream().filter(stack->stack!=null);
		for(ItemStack stack : s.toList()) {
			if(hasBeenChecked(stack, checkedStacks)) continue;
			checkedStacks.add(stack);
			summarized.add(new ItemBuilder(stack).setAmount(getAmountOf(stack, list)).build());
		}
		
		return summarized;
	}
	private static int getAmountOf(ItemStack stack, List<ItemStack> list) {
		int amount = 0;
		for(ItemStack from : list) {
			if(from == null) continue;
			if(from.getType()==stack.getType()&&from.getDurability()==stack.getDurability())amount++;
		}
		
		return amount;
	}
	private static boolean hasBeenChecked(ItemStack stack, List<ItemStack> checkedStacks) {
		boolean checked = false;
		for(ItemStack from : checkedStacks) {
			if(from == null) continue;
			if(from.getType()==stack.getType()&&from.getDurability()==stack.getDurability()) {
				checked = true;
				break;
			}
		}
		return checked;
	}
	public static List<Recipe> getRecipes() {
		return recipes;
	}
	public static String getColorSubID(int sub) {
		switch (sub) {
		case 0: return "(normal)";
		case 1: return "(red)";
		case 2: return "(cactus green)";
		case 3: return "(cacoa beans)";
		case 4: return "(lapis lazuli)";
		case 5: return "(purple)";
		case 6: return "(cyan)";
		case 7: return "(light gray)";
		case 8: return "(gray)";
		case 9: return "(pink)";
		case 10: return "(lime)";
		case 11: return "(yellow)";
		case 12: return "(light blue)";
		case 13: return "(magenta)";
		case 14: return "(orange)";
		case 15: return "(bone meal)";
		
		
		default:
			break;
		}
		return "Unknown";
	}
	public static List<Recipe> filter(String filter) {
		if(filter == null) return recipes;
		List<Recipe> available = new ArrayList<>();
		for(Recipe r : RecipeRegistry.getRecipes()) {
			boolean add = false;
			if(r.getTarget().getType().name().toLowerCase().contains(filter.toLowerCase())) add = true;
			if(r.getTarget().getItemMeta().getDisplayName()!=null&&r.getTarget().getItemMeta().getDisplayName().toLowerCase().contains(filter.toLowerCase())) add = true;
			if(add)
				available.add(r);
			
		}
		return available;
	}
}
