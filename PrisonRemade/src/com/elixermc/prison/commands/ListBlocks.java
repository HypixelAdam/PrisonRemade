package com.elixermc.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;
import com.elixermc.prison.managers.ConfigManager;

public class ListBlocks implements CommandExecutor {
	
	Core plugin = Core.getInstance();
	String prefix = Core.getInstance().getConfig().getString("Settings.prefix");
	ConfigManager cm = new ConfigManager();

	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] a) {
		if (cm == null) {
			cm = new ConfigManager();
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a 'Player' to run this command!");
			return true;
		}
		Player p = (Player) sender;
		int length = a.length;
		if (cmd.getName().equalsIgnoreCase("prlistblocks")) {
			if (!p.hasPermission("prisonremade.command.listblocks")) {
				p.sendMessage(format(prefix + "&cYou don't have permission to run this command."));
				return true;
			}
			if (length == 0) {
				p.sendMessage(format(prefix+"&cYou must define a name. &6Correct Usage: /prlistblocks <name> [page] OR /prlb <name> [page]"));
				return true;
			} else if (length == 1) {
				String name = a[0];
				if (!cm.doesMineExist(name)) {
					p.sendMessage(format(prefix+"&cThat mine doesn't exist!"));
					return true;
				}
				cm.listBlocks(p, name);
				return true;
			} else if (length == 2) {
				String name = a[0];
				int page = Integer.parseInt(a[1]);
				if (!cm.doesMineExist(name)) {
					p.sendMessage(format(prefix+"&cThat mine doesn't exist!"));
					return true;
				}
				if (page < 0) {
					p.sendMessage(format(prefix+"&cInvalid page number."));
					return true;
				}
				cm.listBlocks(p, name, page);
				return true;
			} else if (length >= 3) {
				p.sendMessage(format(prefix+"&cToo many arguments! &6Correct Usage: /prlistblocks <name> [page] OR /prlb <name> [page]"));
				return true;
			}
		}
		return true;
	}

	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
