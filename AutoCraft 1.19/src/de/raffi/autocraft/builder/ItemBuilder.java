package de.raffi.autocraft.builder;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder implements Cloneable{

	private ItemStack item;
	private ItemMeta itemMeta;

	public ItemBuilder(Material material, int subID) {
		this(material, (short)subID);
	}
	public ItemBuilder(Material material, short subID) {
		item = new ItemStack(material, 1, subID);
		itemMeta = item.getItemMeta();
	}
	public ItemBuilder(Material material) {
		this(material, (short)0);
	}
	public ItemBuilder(ItemStack stack) {
		this.item = stack;
		this.itemMeta = stack.getItemMeta();
	}
	public ItemBuilder setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	public String getName() {
		return itemMeta.getDisplayName();
	}
	public ItemBuilder glow() {
		addEnchantment(Enchantment.DURABILITY, 1);
		addFlags(ItemFlag.HIDE_ENCHANTS);
		return this;
	}
	public ItemBuilder glow(boolean glow) {
		if(glow) {
			addEnchantment(Enchantment.DURABILITY, 1);
			addFlags(ItemFlag.HIDE_ENCHANTS);
		}
		return this;
	}
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	public ItemBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}
	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		itemMeta.addEnchant(enchantment, level, true);
		return this;
	}
	public ItemBuilder addFlags(ItemFlag... flag) {
		itemMeta.addItemFlags(flag);
		return this;
	}
	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		item.removeEnchantment(enchantment);
		return this;
	}
	public ItemBuilder setData(MaterialData data) {
		item.setData(data);
		return this;
	}
	public ItemBuilder setDurability(int d) {
		item.setDurability((short) d);
		return this;
	}
	public int getDurability() {
		return item.getDurability();
	}
	public ItemBuilder setUnbreakable(boolean state) {
		itemMeta.spigot().setUnbreakable(state);
		return this;
	}
	public ItemStack build() {
		item.setItemMeta(itemMeta);
		return item;
	}
	public ItemBuilder copy() {
		try {
			return (ItemBuilder) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
