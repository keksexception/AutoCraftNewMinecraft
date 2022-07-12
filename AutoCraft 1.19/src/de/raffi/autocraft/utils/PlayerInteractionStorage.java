package de.raffi.autocraft.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.raffi.autocraft.blocks.BasicBlock;

public class PlayerInteractionStorage {
	
	private static HashMap<Player, BasicBlock> currentBlock = new HashMap<>();
	
	public static List<UUID> armorstands = new ArrayList<>();
	
	public static BasicBlock getCurrentBlock(Player p) {
		return currentBlock.get(p);
	}
	public static void setCurrentBlock(Player p, BasicBlock block) {
		currentBlock.put(p, block);
	}
}
