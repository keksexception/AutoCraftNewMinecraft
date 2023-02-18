package de.raffi.autocraft.blocks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.raffi.autocraft.config.Messages;
import de.raffi.autocraft.converter.ConverterLocation;
import de.raffi.autocraft.utils.JSONConverter;

public class BlockOreAnalysizer extends QueueableConnectedBlock{

	public BlockOreAnalysizer(Material material, int subID, Location location, Inventory inventory) {
		super(material, subID, location, inventory);
		// TODO Auto-generated constructor stub
	}

	public BlockOreAnalysizer(Material material, int subID, Location location) {
		super(material, subID, location);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Inventory getDefaultInventory() {
		return Bukkit.createInventory(null, 9*2,getInternInventoryName());
	}

	@Override
	public void addItemToInventory(ItemStack item) {
		for(int amount = 0; amount < item.getAmount(); amount++) {
			if(item.getType()!=Material.COBBLESTONE) {
				queueInventory.addItem(new ItemStack(item.getType()));
			} else {
				if(new Random().nextInt(100)<Messages.DIAMOND_PROBABILITY) { 
					queueInventory.addItem(new ItemStack(Material.DIAMOND));
				} else {
					queueInventory.addItem(new ItemStack(Material.DIRT));
				}
			}
		}
	}
	@Override
	public void update() {		
		if(getInventory().firstEmpty()==-1) {
			return;
		}
		try {
			for(Block connected : getConnected()) {
				if(connected.getType()!=Material.HOPPER) continue;
				Hopper connectedHopper = (Hopper) connected.getState();
				for(ItemStack s : connectedHopper.getInventory().getContents()) {
					if(s == null) continue;
					if(s.getType()==Material.AIR)  continue;
					addItemToInventory(s);
					removeFromInv(connectedHopper.getInventory(), s.getType(), s.getAmount());
					break;
				}
			}
		} catch (Exception e) {
			
		}
	
		
		Block under = getWorld().getBlockAt(getLocation().clone().add(0, -1, 0));
		if(under.getType()!=Material.HOPPER) return;
		
		Hopper hopper = (Hopper) under.getState();
		if(hopper.getBlock().isBlockPowered()||hopper.getBlock().isBlockIndirectlyPowered()) return;
		for (ItemStack item : queueInventory.getContents()) {
			if (item == null)
				continue;
			if (item.getType() == Material.AIR)
				continue;
			if (hopper.getInventory().firstEmpty() == -1)
				continue;

			hopper.getInventory().addItem(item);
			removeFromInv(queueInventory, item.getType(), item.getAmount());

		}

	}
	@Override
	public BasicBlock create() {		
		queueInventory = Bukkit.createInventory(null, 9*5);
		
		return super.create();
	}

	@SuppressWarnings("unchecked")
	public static BlockOreAnalysizer fromJSON(JSONObject obj) throws Exception {
		Material material = Material.valueOf((String) obj.get("material"));
		long subID = (long) obj.get("subID");
		Location location = new ConverterLocation().create((String)obj.get("location"));
		Inventory inventory = JSONConverter.inventoryFromJson((JSONObject) obj.get("inventory"));
		
		
		BlockOreAnalysizer blockAnalyser = new BlockOreAnalysizer(material, (int) subID, location, inventory);
		blockAnalyser.create();
		
		JSONArray connected = (JSONArray) obj.get("connectedBlocks");
		connected.forEach(s->{
			Location loc = new ConverterLocation().create((String) s);
			blockAnalyser.addConnected(loc.getWorld().getBlockAt(loc));
		});

		return blockAnalyser;
	}


	@Override
	public String getInternInventoryName() {
		return "OreAnalyzer";
	}

}
