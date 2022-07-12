package de.raffi.autocraft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.raffi.autocraft.blocks.BasicBlock;
import de.raffi.autocraft.main.AutoCraft;

public class BlockManager {
	
	
	private static List<BasicBlock> blocks;
	
	public static void init() {
		blocks = new ArrayList<>();
		runUpdateLoop();
	}
	public static void registerBlock(BasicBlock block) {
		blocks.add(block);
		block.create();
	}
	public static void unregisterBlock(BasicBlock block) {
		blocks.remove(block);
		block.remove(true);
	}
	public static List<BasicBlock> getBlocks() {
		return blocks;
	}
	public static BasicBlock getBlockAt(Location loc) {
		for(BasicBlock block : blocks) {
			if(!block.getWorld().equals(loc.getWorld())) continue;
			if(block.getLocation().getBlock().equals(loc.getBlock())) return block;
		}
		return null;
	}
	public static BasicBlock getBlockAt(Block block) {
		for(BasicBlock bblock : blocks) {
			if(bblock.getLocation().getBlock().equals(block)) return bblock;
		}
		return null;
	}
	private static void runUpdateLoop() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoCraft.getAutoCraft(), ()->{
			blocks.forEach(block->block.update());
		}, 60, 5);
	}
	private static File dataFile = new File("plugins/AutoCraft/blockdata.json");
	@SuppressWarnings("unchecked")
	public static void saveBlocks() {
			try {
				
				if(blocks.size()==0) return;
				
				if(!dataFile.getParentFile().exists())
					dataFile.getParentFile().mkdir();
				
				if(!dataFile.exists())
					dataFile.createNewFile();
				
				JSONArray blocks = new JSONArray();
				BlockManager.blocks.forEach(block->blocks.add(block.toJson()));
				
				FileWriter writer = new FileWriter(dataFile);
				writer.write(blocks.toJSONString());
				writer.close();
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("[AutoCraft] Unable to save blocks");
			}
	}
	public static void readBlocks() {
		if(!dataFile.exists()) return;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String json = reader.readLine();
			assert json!=null : "[AutoCraft] Unable to read blocks: JSON is null";
			JSONArray jarray = (JSONArray) new JSONParser().parse(json);
			for(Object obj : jarray.stream().toList()) {
				JSONObject jobj = (JSONObject) obj;
				@SuppressWarnings("unchecked")
				Class<? extends BasicBlock> relatedClass = (Class<? extends BasicBlock>) Class.forName((String) jobj.get("classname"));
				Method m = relatedClass.getDeclaredMethod("fromJSON", JSONObject.class);
				blocks.add((BasicBlock) m.invoke(null, jobj));
			}
			reader.close();
			System.out.println("[AutoCraft] Loaded " + blocks.size() + " blocks.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[AutoCraft] Unable to read blocks");
		}
		
	}

}
