package de.raffi.autocraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.raffi.autocraft.config.Messages;

public class CommandAutoCraft implements CommandExecutor {

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
					Messages.loadMessages();
					p.sendMessage(Messages.PREFIX + " §aReloading complete");
					break;

				default:
					showHelp(p);
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
		p.sendMessage(Messages.PREFIX + " §7/autocraft help §eshow help");
		p.sendMessage(Messages.PREFIX + " §7/autocraft reload §ereload all messages");
	}

}
