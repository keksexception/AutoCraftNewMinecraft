package de.raffi.autocraft.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.raffi.autocraft.converter.ConverterLocation;
import de.raffi.autocraft.recipes.Recipe;
import de.raffi.autocraft.recipes.RecipeRegistry;
import de.raffi.autocraft.utils.JSONConverter;

public class BlockCrusher extends QueueableConnectedBlock implements Interactable{
	
	

	public BlockCrusher(Material material, int subID, Location location, Inventory inventory) {
		super(material, subID, location, inventory);
	}

	@Override
	public Inventory getDefaultInventory() {
		return Bukkit.createInventory(null, 9*2, getInternInventoryName());
	}

	@Override
	public BasicBlock create() {
		queueInventory = Bukkit.createInventory(null, 9*5);
		return super.create();
	}
	@Override
	public void addItemToInventory(ItemStack item) {
		Recipe foundRecipe = null;
		getInventory().addItem(item);
		for(int i = 0; i < item.getAmount(); i++) {
			for(ItemStack available : getInventory().getContents()) {
				if(available==null)continue;
				Material availableType = available.getType();
				int availableCount = available.getAmount();
				for(Recipe recipe : RecipeRegistry.getRecipes()) {
					if(recipe.getTarget().getType()==availableType && availableCount >= recipe.getTarget().getAmount()) {
						for(ItemStack ingrediants : recipe.getIngrediants()) {
							getQueueInventory().addItem(ingrediants);
							
						}
						foundRecipe = recipe;
						break;
					}
				}
				if(foundRecipe!=null) {
					removeFromInv(getInventory(), foundRecipe.getTarget().getType(), foundRecipe.getTarget().getAmount());				
					foundRecipe = null;
					break;
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
			e.printStackTrace();
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


	@SuppressWarnings("unchecked")
	public static BlockCrusher fromJSON(JSONObject obj) throws Exception {
		Material material = Material.valueOf((String) obj.get("material"));
		long subID = (long) obj.get("subID");
		Location location = new ConverterLocation().create((String)obj.get("location"));
		Inventory inventory = JSONConverter.inventoryFromJson((JSONObject) obj.get("inventory"));

		
		BlockCrusher blockCrusher = new BlockCrusher(material, (int) subID, location, inventory);
		blockCrusher.create();
		
		JSONArray connected = (JSONArray) obj.get("connectedBlocks");
		connected.forEach(s->{
			Location loc = new ConverterLocation().create((String) s);
			blockCrusher.addConnected(loc.getWorld().getBlockAt(loc));
		});

		return blockCrusher;
	}

	@Override
	public void onInteract(Player p) {
		p.openInventory(getQueueInventory());
		
	}


	@Override
	public String getInternInventoryName() {
		return "BlockCrusher";
	}
}
