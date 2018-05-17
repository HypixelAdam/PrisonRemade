package com.elixermc.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.elixermc.prison.Core;
import com.elixermc.prison.managers.ConfigManager;

public class AddBlock implements CommandExecutor {
	
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
		if (cmd.getName().equalsIgnoreCase("praddblock")) {
			if (!p.hasPermission("prisonremade.command.addblock")) {
				p.sendMessage(format(prefix + "&cYou don't have permission to run this command."));
				return true;
			}
			if (length == 0) {
				p.sendMessage(format(prefix+"&cYou must define a name. &6Correct Usage: /praddblock <name> <block id> [data] OR /prab <name> <block id> [data]"));
				return true;
			} else if (length == 1) {
				p.sendMessage(format(prefix+"&cYou must define a block (in number format). &6Correct Usage: /praddblock <name> <block id> [data] OR /prab <name> <block id> [data]"));
				return true;
			} else if (length == 2) {
				String name = a[0];
				String block = a[1]+",0";
				if (!cm.doesMineExist(name)) {
					p.sendMessage(format(prefix+"&cThat mine doesn't exist!"));
					return true;
				}
				if (cm.doesBlockExist(name, block)) {
					p.sendMessage(format(prefix+"&cThat block already exists!"));
					return true;
				}
				cm.addBlock(p,name,block);
				return true;
			} else if (length == 3) {
				String name = a[0];
				String block = a[1]+","+a[2];
				if (!cm.doesMineExist(name)) {
					p.sendMessage(format(prefix+"&cThat mine doesn't exist!"));
					return true;
				}
				if (cm.doesBlockExist(name, block)) {
					p.sendMessage(format(prefix+"&cThat block already exists!"));
					return true;
				}
				cm.addBlock(p, name, block);
				return true;
			} else if (length >= 4) {
				p.sendMessage(format(prefix+"&cToo many arguments! &6Correct Usage: /praddblock <name> <block id> [data] OR /prab <name> <block id> [data]"));
				return true;
			}
		}
		return true;
	}

	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
