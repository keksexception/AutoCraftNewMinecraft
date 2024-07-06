package de.raffi.autocraft.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.raffi.autocraft.builder.ItemBuilder;
import de.raffi.autocraft.config.Messages;
import de.raffi.autocraft.converter.ConverterLocation;
import de.raffi.autocraft.recipes.Recipe;
import de.raffi.autocraft.utils.JSONConverter;
import de.raffi.autocraft.utils.PlayerInteractionStorage;

public class BlockAutoCrafter extends QueueableConnectedBlock implements Interactable{
	
	private Recipe target;
	
	private Inventory queueInventory;

	public BlockAutoCrafter(Material material, int subID, Location location, Inventory inventory, Recipe target) {
		super(material, subID, location, inventory);
		this.target = target;
	}

	@Override
	public Inventory getDefaultInventory() {
		Inventory inv = Bukkit.createInventory(null, 9*2, getInternInventoryName()); //intern inventory
		
		return inv;
	}

	@Override
	public void onInteract(Player p) {
		PlayerInteractionStorage.setCurrentBlock(p, this);
		Inventory open = Bukkit.createInventory(null, 9, Messages.INVENTORY_TITLE_AUTOCRAFTER_MENUE);
		open.setItem(3,new ItemBuilder(Material.PAPER).setName(Messages.ITEM_RECIPES_NAME).setLore(Messages.ITEM_RECIPES_LORE).build());
		open.setItem(4,new ItemBuilder(Material.CHEST).setName(Messages.ITEM_INTERNINV_NAME).setLore(Messages.ITEM_INTERNINV_LORE).build());
		open.setItem(5,new ItemBuilder(Material.DIAMOND).setName(Messages.ITEM_OVERFLOW_NAME).setLore(Messages.ITEM_OVERFLOW_LORE).build());
		p.openInventory(open);
		
	
	}
	@Override
	public BasicBlock create() {
		
		queueInventory = Bukkit.createInventory(null, 9*5);
		
		try {
			getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
		} catch (NoSuchFieldError e) {
			e.printStackTrace();
		}
		
		return super.create();
	}
	@Override
	public void addItemToInventory(ItemStack item) {
		super.addItemToInventory(item);
		craftTarget();
		
		
	}

	public void craftTarget() {
		if (!canCraftTarget())
			return;

		//remove ingrediants
		for (ItemStack ingrediant : target.getIngrediants()) {
			removeFromInv(getInventory(),ingrediant.getType(), ingrediant.getAmount());
		}

		queueInventory.addItem(target.getTarget());
		
		craftTarget();
	}
	@SuppressWarnings("unused")
	private ItemStack findItemStackWith(Material m) {
		for(ItemStack inventoryItem : getInventory().getContents()) {
			if(inventoryItem.getType() == m) return inventoryItem;
		}
		return null;
	}

	public boolean canCraftTarget() {
		boolean canCraft = false;
		
		for(ItemStack ingrediant : target.getIngrediants()) {
			if(getInventory().contains(ingrediant.getType(), ingrediant.getAmount())) {
				canCraft = true;
			} else {
				canCraft = false;
				break;
			}
				
		}
		
		return canCraft;
	}
	private boolean contains(Material type, int amount) {
		for(int i = 0; i < getInventory().getSize(); i++) {
			ItemStack stack = getInventory().getContents()[i];
			System.out.println(stack.getType());
			if(stack == null) continue;
			if(stack.getType() == type) {
				int counter = 0;
				for(int j = 0; j < getInventory().getSize(); j++) {
					ItemStack count = getInventory().getContents()[j];
					if(stack.getType()!=type) continue;
					counter+=count.getAmount();
				}
				if(counter>=amount) return true;
			}
		}
		return false;
	}

	public Recipe getTarget() {
		return target;
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
	@Override
	public void remove(boolean b) {
		super.remove(b);
	}
	public Inventory getQueueInventory() {
		return queueInventory;
	}
	public void setTarget(Recipe target) {
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJson() {
		JSONObject block = super.toJson();
		block.put("recipe", getTarget().toJson());
		return block;
	}
	@SuppressWarnings("unchecked")
	public static BlockAutoCrafter fromJSON(JSONObject obj) throws Exception {
		Material material = Material.valueOf((String) obj.get("material"));
		long subID = (long) obj.get("subID");
		Location location = new ConverterLocation().create((String)obj.get("location"));
		Inventory inventory = JSONConverter.inventoryFromJson((JSONObject) obj.get("inventory"));
		
		Recipe r = Recipe.fromJson((JSONObject) obj.get("recipe"));
		
		BlockAutoCrafter blockAutoCraft = new BlockAutoCrafter(material, (int) subID, location, inventory, r);
		blockAutoCraft.create();
		
		JSONArray connected = (JSONArray) obj.get("connectedBlocks");
		connected.forEach(s->{
			Location loc = new ConverterLocation().create((String) s);
			blockAutoCraft.addConnected(loc.getWorld().getBlockAt(loc));
		});

		return blockAutoCraft;
	}

	@Override
	public String getInternInventoryName() {
		return "AutoCraft";
	}
}
