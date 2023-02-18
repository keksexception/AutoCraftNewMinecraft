package de.raffi.autocraft.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.raffi.autocraft.builder.ItemBuilder;
import de.raffi.autocraft.config.Messages;
import de.raffi.autocraft.converter.ConverterLocation;
import de.raffi.autocraft.utils.JSONConverter;
import de.raffi.autocraft.utils.PlayerInteractionStorage;

public class BlockAutoEnchanter extends QueueableConnectedBlock implements Interactable {

	public BlockAutoEnchanter(Location location, Inventory inventory) {
		super(Material.ENCHANTING_TABLE, 0, location, inventory);
	}

	@Override
	public Inventory getDefaultInventory() {
		Inventory inv = Bukkit.createInventory(null, 9*1, getInternInventoryName()); //intern inventory
		return inv;
	}

	@Override
	public void onInteract(Player p) {
		PlayerInteractionStorage.setCurrentBlock(p, this);
		Inventory open = Bukkit.createInventory(null, 9, Messages.INVENTORY_TITLE_SELECTOPTION);
		open.setItem(3,new ItemBuilder(Material.CHEST).setName(Messages.ITEM_INTERNINV_NAME).setLore(Messages.ITEM_INTERNINV_LORE).build());
		open.setItem(5,new ItemBuilder(Material.DIAMOND).setName(Messages.ITEM_OVERFLOW_NAME).setLore(Messages.ITEM_OVERFLOW_LORE).build());
		p.openInventory(open);
		
	}
	@Override
	public BasicBlock create() {
		queueInventory = Bukkit.createInventory(null, 9*5);
		return super.create();
	}
	@Override
	public void addItemToInventory(ItemStack item) {
		super.addItemToInventory(item);

		
		for(ItemStack content : getInventory().getContents()) {
			if(content==null|| content.getType() == Material.AIR) continue;
			if(content.getType()==Material.ENCHANTED_BOOK) {
				EnchantmentStorageMeta esm = (EnchantmentStorageMeta) content.getItemMeta();
				esm.getStoredEnchants().forEach((enchant,level)->{
					for(ItemStack check : getInventory().getContents()) {
						if(check==null|| check.getType() == Material.AIR) continue;
						if(enchant.canEnchantItem(check)) {
							getInventory().remove(check);
							getInventory().remove(content);
							check.addEnchantments(esm.getStoredEnchants());
							getQueueInventory().addItem(check);
							break;
						}
					}				
				});
				break;
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
	@SuppressWarnings("unchecked")
	public static BlockAutoEnchanter fromJSON(JSONObject obj) throws Exception {
		Location location = new ConverterLocation().create((String)obj.get("location"));
		Inventory inventory = JSONConverter.inventoryFromJson((JSONObject) obj.get("inventory"));
		
		
		BlockAutoEnchanter blockAutoEnchanter= new BlockAutoEnchanter(location, inventory);
		blockAutoEnchanter.create();
		
		JSONArray connected = (JSONArray) obj.get("connectedBlocks");
		connected.forEach(s->{
			Location loc = new ConverterLocation().create((String) s);
			blockAutoEnchanter.addConnected(loc.getWorld().getBlockAt(loc));
		});

		return blockAutoEnchanter;
	}

	@Override
	public String getInternInventoryName() {
		return "AutoEnchant";
	}
}
