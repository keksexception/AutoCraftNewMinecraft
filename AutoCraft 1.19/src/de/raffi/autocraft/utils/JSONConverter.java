package de.raffi.autocraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONObject;

import de.raffi.autocraft.builder.ArmorBuilder;
import de.raffi.autocraft.builder.ItemBuilder;
import de.raffi.autocraft.builder.SkullBuilder;

public class JSONConverter {
	
	
	/**
	 * converts an itemstack into json
	 * @param i the itemstack that should be converted to a jsonobject
	 * @return a jsonobject that contains the data of the itemstack
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject toJson(ItemStack i) {
		JSONObject stack = new JSONObject();
		stack.put("amount", i.getAmount());
		stack.put("durability",i.getDurability());
		stack.put("type", i.getType().toString());
		stack.put("name", i.getItemMeta().getDisplayName());
		ItemType t = ItemType.getType(i);
		stack.put("itemtype", t.name());
		
		if(t==ItemType.SKULL)
			stack.put("skullowner", ((SkullMeta)i.getItemMeta()).getOwner());
		else if(t==ItemType.COLORED_ARMOR) {
			LeatherArmorMeta armormeta = (LeatherArmorMeta) i.getItemMeta();
			stack.put("color", armormeta.getColor().asRGB());
		}
		
	
		return stack;
	}
	/**
	 * 
	 * @param o the jsonobject the data should be read from
	 * @return the itemstack from the jsondata
	 */
	public static ItemStack fromJson(JSONObject o) {
		long amount = (long) o.get("amount");
		short durability = Short.valueOf(String.valueOf(o.get("durability")));
		Material type = Material.valueOf((String) o.get("type"));
		String name = (String) o.get("name");
		ItemType itemType = ItemType.valueOf((String) o.get("itemtype"));
		if(itemType==ItemType.SKULL)
			return new SkullBuilder((String)o.get("skullowner")).setName(name).setAmount((int)amount).build();
		else if(itemType==ItemType.COLORED_ARMOR)
			return new ArmorBuilder(type,durability).setAmount((int) amount).setName(name).setColor(Color.fromRGB((int)(long)o.get("color"))).build();
		else 
			return new ItemBuilder(type, durability).setAmount((int) amount).setName(name).build();
	}
	@SuppressWarnings("unchecked")
	public static JSONObject toJson(Inventory inv, String name) {
		JSONObject inf = new JSONObject();
		inf.put("size", inv.getSize());
		inf.put("name",name);
		for(int i = 0; i < inv.getSize(); i++) {
			if(inv.getItem(i)!=null)
			inf.put("slot"+i, toJson(inv.getItem(i)));
		}
		return inf;
	}
	public static Inventory inventoryFromJson(JSONObject o) {
		long size = (long) o.get("size");
		String name = (String) o.get("name");
		Inventory inv = Bukkit.createInventory(null, (int)size, name);
		for(int i = 0; i < size; i++) {
			if(o.get("slot"+i)!=null)
			inv.addItem(fromJson((JSONObject) o.get("slot"+i)));
		}
		
		return inv;
	}
	public enum ItemType {
		
		
		ITEM, 
		SKULL,
		COLORED_ARMOR;
		
		
		public static ItemType getType(ItemStack i) {
			if(i.getType() == Material.LEGACY_SKULL_ITEM) return SKULL;
			else if(i.getType().name().startsWith("LEATHER_")) return COLORED_ARMOR;
			return ITEM;
		}

	}
}
