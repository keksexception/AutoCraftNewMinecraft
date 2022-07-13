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
import de.raffi.autocraft.listener.InteractionListener;
import de.raffi.autocraft.recipes.RecipeRegistry;
import de.raffi.autocraft.utils.BlockManager;

public class AutoCraft extends JavaPlugin {
	
	private static AutoCraft autoCraft;
	private ItemStack autoCrafter;
	
	@Override
	public void onEnable() {
		autoCraft = this;
		
		this.autoCrafter = new ItemBuilder(Material.LEGACY_WORKBENCH).setName("§eAutoCrafter§5").build();
		
		
		getCommand("autocraft").setExecutor(new CommandAutoCraft());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InteractionListener(), this);
		pm.registerEvents(new HopperHandler(), this);
		
		/*
		 * add custom recipe
		 */
		
		
		ShapedRecipe r = new ShapedRecipe(new NamespacedKey(this, "AutoCrafter"),autoCrafter);
		r.shape("AAA","ABA","AAA");
		r.setIngredient('A', Material.LEGACY_WORKBENCH);
		r.setIngredient('B', Material.REDSTONE_BLOCK);
		Bukkit.getServer().addRecipe(r);
		
		/*
		 * init
		 */
		BlockManager.init();
		RecipeRegistry.init();
		BlockManager.readBlocks();
		Messages.loadMessages();
		
		
	}
	public ItemStack getAutoCrafter() {
		return autoCrafter;
	}
	public static AutoCraft getAutoCraft() {
		return autoCraft;
	}
	@Override
	public void onDisable() {
		BlockManager.saveBlocks();
		BlockManager.getBlocks().forEach(b->b.remove(false));
	}

}
