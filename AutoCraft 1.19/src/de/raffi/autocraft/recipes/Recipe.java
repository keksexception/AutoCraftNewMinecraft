package de.raffi.autocraft.recipes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.raffi.autocraft.utils.JSONConverter;

public class Recipe {
	
	private ItemStack target;
	private ItemStack[] ingrediants;
	public Recipe(ItemStack target, ItemStack... ingrediants) {
		this.target = target;
		this.ingrediants = ingrediants;
	}
	
	public ItemStack getTarget() {
		return target;
	}
	public ItemStack[] getIngrediants() {
		return ingrediants;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject recipe = new JSONObject();
		recipe.put("target", JSONConverter.toJson(getTarget()));
		
		JSONArray ingrediants = new JSONArray();
		for(ItemStack stack : getIngrediants()) {
			ingrediants.add(JSONConverter.toJson(stack));
		}
		
		recipe.put("ingrediants", ingrediants);
		
		return recipe;
	}
	@SuppressWarnings("unchecked")
	public static Recipe fromJson(JSONObject from) {
		ItemStack target = JSONConverter.fromJson((JSONObject) from.get("target"));;
		
		JSONArray jIngrediants = (JSONArray) from.get("ingrediants");
		List<ItemStack> ingrediants = new ArrayList<>();
		jIngrediants.forEach(ingrediant -> ingrediants.add(JSONConverter.fromJson((JSONObject) ingrediant)));
		
		return new Recipe(target,  ingrediants.stream().toArray(ItemStack[]::new));
	}

}
