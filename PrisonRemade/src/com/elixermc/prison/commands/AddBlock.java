package com.elixermc.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;

public class AddBlock implements CommandExecutor {
	
	Core plugin = Core.getInstance();
	String prefix = Core.getInstance().getConfig().getString("Settings.prefix");

	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] a) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a 'Player' to run this command!");
			return true;
		}
		Player p = (Player) sender;
		int length = a.length;
		if (cmd.getName().equalsIgnoreCase("praddblock")) {
			if (!p.hasPermission("prisonremade.command.addblock")) {
				p.sendMessage(format(prefix + "&cYou don't have permission to run this command."));
				return true;
			}
			if (length == 0) {
				p.sendMessage(format(prefix+"&cYou must define a name. &6Correct Usage: /praddblock <name> <block id> [data] OR /prab <name> <block> [data]"));
				return true;
			} else if (length == 1) {
				p.sendMessage(format(prefix+"&cYou must define a block (in number format). &6Correct Usage: /praddblock <name> <block> [data] OR /prab <name> <block> [data]"));
				return true;
			} else if (length == 2) {
				p.sendMessage(format(prefix+"&cYou must define a chance. &6Correct Usage: /praddblock <name> <block> [data] OR /prab <name> <block> [data]"));
				return true;
			} else if (length == 3) {
				
			}
		}
		return true;
	}

	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
