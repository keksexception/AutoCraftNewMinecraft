package de.raffi.autocraft.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public abstract class QueueableConnectedBlock extends ConnectableBlock {

	public QueueableConnectedBlock(Material material, int subID, Location location, Inventory inventory) {
		super(material, subID, location, inventory);
	}

	public QueueableConnectedBlock(Material material, int subID, Location location) {
		super(material, subID, location);
	}
	protected Inventory queueInventory;

	public Inventory getQueueInventory() {
		return queueInventory;
	}
	
}
