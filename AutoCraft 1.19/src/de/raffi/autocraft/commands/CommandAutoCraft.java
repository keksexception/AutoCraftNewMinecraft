package de.raffi.autocraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.raffi.autocraft.config.Messages;
import de.raffi.autocraft.main.AutoCraft;
import de.raffi.autocraft.recipes.RecipeRegistry;
import de.raffi.autocraft.utils.BlockManager;

public class CommandAutoCraft implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		if(p.hasPermission("autocraft.command.use")) {
			switch (args.length) {
			case 0:
				showHelp(p);
				break;
			case 1:
				switch (args[0]) {
				case "reload":
					p.sendMessage(Messages.PREFIX + " §7Reloading files ...");
					p.sendMessage(Messages.PREFIX + " §eReloading messages & settings ...");
					Messages.loadMessages();
					p.sendMessage(Messages.PREFIX + " §eReloading recipes...");
					RecipeRegistry.getRecipes().clear();
					RecipeRegistry.init();
					p.sendMessage(Messages.PREFIX + " §aReloading complete");
					break;
				case "forcesave":
					p.sendMessage(Messages.PREFIX + " §7Saving blocks ...");
					BlockManager.saveBlocks();
					p.sendMessage(Messages.PREFIX + " §aBlock saving complete");
					break;
				default:
					showHelp(p);
					break;
				}
				break;
			case 2:
				switch (args[0]) {
				case "get":
					switch (args[1]) {
					case "autocrafter":
						p.getInventory().addItem(AutoCraft.getAutoCraft().getAutoCrafter());
						p.sendMessage(Messages.PREFIX + " §aYou received autocrafting block");
						break;
					case "autoenchanter":
						p.getInventory().addItem(AutoCraft.getAutoCraft().getAutoEnchanter());
						p.sendMessage(Messages.PREFIX + " §aYou received AutoEnchanter block");
						break;
					case "oreanalyzer":
						p.getInventory().addItem(AutoCraft.getAutoCraft().getOreBlock());
						p.sendMessage(Messages.PREFIX + " §aYou received oreanalyzer block");
						break;
					case "blockcrusher":
						p.getInventory().addItem(AutoCraft.getAutoCraft().getBlockCrusher());
						p.sendMessage(Messages.PREFIX + " §aYou received uncrafting block (blockcrusher)");
						break;
					default:
						break;
					}
					break;

				default:
					break;
				}
				break;
			default:
				showHelp(p);
				break;
			}
		} else 
			p.sendMessage(Messages.PREFIX + " " + Messages.NO_PERMISSION);
		return false;
	}
	private void showHelp(Player p) {
		p.sendMessage(Messages.PREFIX + " §7/autocraft reload §ereload all messages");
		p.sendMessage(Messages.PREFIX + " §7/autocraft forcesave §eforce save all blocks");
		p.sendMessage(Messages.PREFIX + " §7/autocraft get <autocrafter/autoenchanter/oreanalyzer/blockcrusher> §ereceive autocrafter");
		p.sendMessage(Messages.PREFIX + " §7/autocraft help §eshow help");
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		String[] arg1Completions = new String[] {"get","help","reload", "forcesave"};
		if(args.length == 1) {
			for(String check : arg1Completions) {
				if(check.startsWith(args[0].toLowerCase())) 
					suggestions.add(check);
			}
		} else if(args.length == 2) {
			if(args[0].startsWith("get")) {
				String[] getCompletions = new String[] {"autocrafter","autoenchanter","oreanalyzer","blockcrusher"};
				for(String check : getCompletions) {
					if(check.startsWith(args[1].toLowerCase())) 
						suggestions.add(check);
				}
			}
		}
		return suggestions;
	}

}
