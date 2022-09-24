package de.raffi.autocraft.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.raffi.autocraft.builder.ItemBuilder;
import de.raffi.autocraft.commands.CommandAutoCraft;
import de.raffi.autocraft.config.Messages;
import de.raffi.autocraft.listener.HopperHandler;
import de.raffi.autocraft.listener.InputHandler;
import de.raffi.autocraft.listener.InteractionListener;
import de.raffi.autocraft.recipes.RecipeRegistry;
import de.raffi.autocraft.utils.BlockManager;

public class AutoCraft extends JavaPlugin {
	
	private static AutoCraft autoCraft;
	private ItemStack autoCrafter;
	private ItemStack autoEnchanter;
	private ItemStack blockCrusher;
	private ItemStack oreBlock;
	
	@Override
	public void onEnable() {
		autoCraft = this;
		
		Messages.loadMessages();
		
		this.autoCrafter = new ItemBuilder(Material.CRAFTING_TABLE).setName("§eAutoCrafter§5").setLore(Messages.ITEM_AUTOCRAFTER_LORE.split("000")).build();
		this.autoEnchanter = new ItemBuilder(Material.ENCHANTING_TABLE).setName("§5AutoEnchanter").setLore(Messages.ITEM_AUTOENCHANTER_LORE.split("000")).build();
		this.oreBlock = new ItemBuilder(Material.DIAMOND_ORE).setName("§dOreAnalysizer").setLore(Messages.ITEM_OREBLOCK_LORE.split("000")).build();
		this.blockCrusher = new ItemBuilder(Material.NOTE_BLOCK).setName("§cBlockCrusher").setLore(Messages.ITEM_BOCKCRUSHER_LORE.split("000")).build();

		
		CommandAutoCraft cmdAutoCraft = new CommandAutoCraft();
		getCommand("autocraft").setExecutor(cmdAutoCraft);
		getCommand("autocraft").setTabCompleter(cmdAutoCraft);
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InteractionListener(), this);
		pm.registerEvents(new HopperHandler(), this);
		pm.registerEvents(new InputHandler(), this);
		
		/*
		 * add custom recipe
		 */
		
		if(Messages.ALLOW_CRAFT_AUTOCRAFT) {
			ShapedRecipe r = new ShapedRecipe(new NamespacedKey(this, "AutoCrafter"),autoCrafter);
			r.shape("AAA","ABA","AAA");
			r.setIngredient('A', Material.CRAFTING_TABLE);
			r.setIngredient('B', Material.REDSTONE_BLOCK);
			Bukkit.getServer().addRecipe(r);
		}
		if(Messages.ALLOW_CRAFT_AUTOENCHANT) {
			ShapedRecipe r2 = new ShapedRecipe(new NamespacedKey(this, "AutoEnchanter"),autoEnchanter);
			r2.shape("AAA","ABA","AAA");
			r2.setIngredient('A', Material.OBSIDIAN);
			r2.setIngredient('B', Material.ENCHANTING_TABLE);
			Bukkit.getServer().addRecipe(r2);
		}
		if(Messages.ALLOW_CRAFT_OREBLOCK) {
			ShapedRecipe r3 = new ShapedRecipe(new NamespacedKey(this, "OreAnalyzer"),oreBlock);
			r3.shape("AAA","ABA","AAA");
			r3.setIngredient('A', Material.DIAMOND_BLOCK);
			r3.setIngredient('B', Material.REDSTONE_BLOCK);
			Bukkit.getServer().addRecipe(r3);
		}
		if(Messages.ALLOW_CRAFT_BLOCKCRUSHER) {
			ShapedRecipe r4 = new ShapedRecipe(new NamespacedKey(this, "BlockCrusher"),blockCrusher);
			r4.shape("AAA","ABA","AAA");
			r4.setIngredient('A', Material.ANVIL);
			r4.setIngredient('B', Material.REDSTONE_BLOCK);
			Bukkit.getServer().addRecipe(r4);
		}
		
	
		

		
		/*
		 * init
		 */
		BlockManager.init();
		RecipeRegistry.init();
		BlockManager.readBlocks();
		

		
		
	}
	public ItemStack getAutoCrafter() {
		return autoCrafter;
	}
	public ItemStack getAutoEnchanter() {
		return autoEnchanter;
	}
	public ItemStack getBlockCrusher() {
		return blockCrusher;
	}
	public static AutoCraft getAutoCraft() {
		return autoCraft;
	}
	public ItemStack getOreBlock() {
		return oreBlock;
	}
	@Override
	public void onDisable() {
		BlockManager.saveBlocks();
		BlockManager.getBlocks().forEach(b->b.remove(false));
	}

}
