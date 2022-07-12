package de.raffi.autocraft.builder;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

public class ArmorBuilder {

	private ItemStack item;
	private LeatherArmorMeta itemMeta;

	public ArmorBuilder(Material material, int subID) {
		this(material, (short)0);
	}
	public ArmorBuilder(Material material, short subID) {
		item = new ItemStack(material, 1, subID);
		itemMeta = (LeatherArmorMeta) item.getItemMeta();
	}
	public ArmorBuilder(Material material) {
		this(material, (short)0);
	}
	public ArmorBuilder setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	public String getName() {
		return itemMeta.getDisplayName();
	}
	public ArmorBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	public ArmorBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}
	public ArmorBuilder addEnchantment(Enchantment enchantment, int level) {
		itemMeta.addEnchant(enchantment, level, true);
		return this;
	}
	public ArmorBuilder removeEnchantment(Enchantment enchantment) {
		item.removeEnchantment(enchantment);
		return this;
	}
	public ArmorBuilder setData(MaterialData data) {
		item.setData(data);
		return this;
	}
	public ArmorBuilder setColor(Color color) {
		itemMeta.setColor(color);
		return this;
	}
	public ItemStack build() {
		item.setItemMeta(itemMeta);
		return item;
	}

}
