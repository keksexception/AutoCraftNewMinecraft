package de.raffi.autocraft.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import de.raffi.autocraft.converter.ConverterLocation;
import de.raffi.autocraft.utils.JSONConverter;

public abstract class BasicBlock {
	
	private Material material;
	private int subID;
	private Location location;
	private Inventory inventory;
	
	
	public BasicBlock(Material material, int subID, Location location, Inventory inventory) {
		this.material = material;
		this.subID = subID;
		this.location = location;
		this.inventory = inventory;
	}
	public BasicBlock(Material material, int subID, Location location) {
		this(material, subID, location, null);
	}
	public Inventory getInventory() {
		return inventory;
	}
	public Material getMaterial() {
		return material;
	}
	public int getSubID() {
		return subID;
	}

	public Location getLocation() {
		return location;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	public void addItemToInventory(ItemStack item) {
		getInventory().addItem(item);
	}
	public void removeFromInv(Inventory from, Material m, int amount) { 
		int removed = 0;
		for(int i = 0; i < from.getSize();i++) {
			ItemStack itemAt = from.getItem(i);
			if(itemAt==null) continue;
			if(itemAt.getType()!=m) continue;
			
			int itemAmount = itemAt.getAmount();
			
			if(itemAmount>=amount-removed) {
				itemAt.setAmount(itemAmount-amount+removed);
				
				if(itemAmount-amount+removed==0)
					from.setItem(i, null);
				break;
			} else {
				removed+=itemAt.getAmount();
				from.setItem(i, null);
			}
			if(removed == amount) break;
		}
	}
	public World getWorld() {
		return getLocation().getWorld();
	}
	public BasicBlock create() {
		if(inventory == null)
			inventory = getDefaultInventory();
		return this;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject block = new JSONObject();
		block.put("classname", getClass().getName());
		block.put("material", material.name()); 
		block.put("subID", subID);
		block.put("location", new ConverterLocation().stringify(getLocation()));
		block.put("inventory", JSONConverter.toJson(getInventory(),getInternInventoryName()));
		return block;
	}

	public void remove(boolean dropItems) {
		if(!dropItems) return;
		for(ItemStack item : getInventory().getContents()) {
			if(item == null) continue;
			getLocation().getWorld().dropItem(getLocation(), item);
		}
	}
	public void update() {}
	public abstract Inventory getDefaultInventory();
	public abstract String getInternInventoryName();
}
