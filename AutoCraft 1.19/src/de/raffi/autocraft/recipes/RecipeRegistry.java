package de.raffi.autocraft.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import de.raffi.autocraft.builder.ItemBuilder;

public class RecipeRegistry {
	
	private static List<Recipe> recipes;
	
	public static void add(Recipe r) {
		recipes.add(r);
	}
	public static void init() {
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
		System.out.println("[AutoCraft] Registering recipes completed");
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
}
