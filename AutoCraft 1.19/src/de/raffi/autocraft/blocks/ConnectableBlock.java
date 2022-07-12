package de.raffi.autocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.raffi.autocraft.converter.ConverterLocation;


public abstract class ConnectableBlock extends BasicBlock {

	public ConnectableBlock(Material material, int subID, Location location) {
		super(material, subID, location);
	}

	public ConnectableBlock(Material material, int subID, Location location, Inventory inventory) {
		super(material, subID, location, inventory);
	}
	
	private List<Block> connected;
	
	@Override
	public BasicBlock create() {
		connected = new ArrayList<>();
		return super.create();
	}
	public List<Block> getConnected() {
		return connected;
	}
	public void addConnected(Block block) {
		connected.add(block);
	}
	public void removeConnected(Block block) {
		connected.remove(block);
	}
	public boolean isConnected(Block block) {
		boolean connected = false;
		for(Block b : getConnected())
			if(b.equals(block))  {
				connected = true;
				break;
			}
		return connected;
	}
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJson() {
		JSONObject block = super.toJson();
		JSONArray connected = new JSONArray();
		getConnected().forEach(con->connected.add(new ConverterLocation().stringify(con.getLocation())));
		block.put("connectedBlocks", connected);
		return block;
	}
}
