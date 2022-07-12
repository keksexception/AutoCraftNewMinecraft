package de.raffi.autocraft.builder;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullBuilder {

	private String skullOwner;
	private ItemStack item;
	private SkullMeta skullMeta;


	public SkullBuilder(Player skullOwner) {
		this.skullOwner = skullOwner.getName();
		this.item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)3);
		this.skullMeta = (SkullMeta) item.getItemMeta();
	}
	public SkullBuilder(String skullOwner) {
		this.skullOwner = skullOwner;
		this.item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short)3);
		this.skullMeta = (SkullMeta) item.getItemMeta();
	}
	public SkullBuilder setLore(String... lore) {
		skullMeta.setLore(Arrays.asList(lore));
		return this;
	}
	public SkullBuilder setName(String name) {
		skullMeta.setDisplayName(name);
		return this;
	}
	public ItemStack build() {
		skullMeta.setOwner(skullOwner);
		item.setItemMeta(skullMeta);
		return item;
	}
	public SkullBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
}
